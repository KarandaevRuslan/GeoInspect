package com.karandaev.geo_inspect.core.presentation.settings

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karandaev.geo_inspect.core.domain.model.AppSettings
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.ThemeMode
import com.karandaev.geo_inspect.core.domain.repository.InspectionReportRepository
import com.karandaev.geo_inspect.core.domain.repository.SettingsRepository
import com.karandaev.geo_inspect.core.presentation.viewModelFactory
import com.karandaev.geo_inspect.core.inspection_api.InspectionProvider
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SettingsSharingTimeoutMillis = 5_000L
private const val SavedServerAvailabilityAutoCheckMinIntervalMillis = 60_000L

/**
 * ViewModel for app settings.
 *
 * Exposes persisted settings, runtime-only dialog state, and handles settings updates,
 * note imports, resetting notes to default seed data, road crack backend URL synchronization,
 * and server availability checks.
 */
class SettingsViewModel(
  private val settingsRepository: SettingsRepository,
  private val inspectionReportRepository: InspectionReportRepository,
  private val inspectionProvider: InspectionProvider
) : ViewModel() {

  // -----------------------------------------------------------------------------------------------
  // Runtime state
  // -----------------------------------------------------------------------------------------------

  private val _runtimeUiState = MutableStateFlow(
    SettingsRuntimeUiState()
  )

  /**
   * Runtime-only settings UI state.
   */
  val runtimeUiState: StateFlow<SettingsRuntimeUiState> = _runtimeUiState.asStateFlow()

  private var hasCheckedInitialNonBlankServerBaseUrl = false

  private var lastSavedServerAvailabilityCheckElapsedRealtime = 0L

  private val savedServerAvailabilityCheck = ServerAvailabilityCheck()
  private val draftServerAvailabilityCheck = ServerAvailabilityCheck()

  private var appliedServerBaseUrl: String? = null

  // -----------------------------------------------------------------------------------------------
  // Persisted settings state
  // -----------------------------------------------------------------------------------------------

  /**
   * Current app settings.
   */
  val settings: StateFlow<AppSettings> = settingsRepository.observeSettings()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Companion.WhileSubscribed(SettingsSharingTimeoutMillis),
      initialValue = AppSettings()
    )

  /**
   * Currently selected theme mode.
   */
  val themeMode: StateFlow<ThemeMode> = settings
    .map { appSettings ->
      appSettings.themeMode
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Companion.WhileSubscribed(SettingsSharingTimeoutMillis),
      initialValue = AppSettings().themeMode
    )

  /**
   * Currently selected language code.
   */
  val languageCode: StateFlow<String> = settings
    .map { appSettings ->
      appSettings.languageCode
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Companion.WhileSubscribed(SettingsSharingTimeoutMillis),
      initialValue = AppSettings().languageCode
    )

  /**
   * Currently configured road crack backend server base URL.
   */
  val serverBaseUrl: StateFlow<String> = settings
    .map { appSettings ->
      appSettings.serverBaseUrl
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Companion.WhileSubscribed(SettingsSharingTimeoutMillis),
      initialValue = AppSettings().serverBaseUrl
    )

  // -----------------------------------------------------------------------------------------------
  // Lifecycle
  // -----------------------------------------------------------------------------------------------

  init {
    ensureDefaultSettings()
    observeServerBaseUrl()
  }

  // -----------------------------------------------------------------------------------------------
  // Dialog actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Shows the provided settings dialog.
   */
  fun setDialogState(dialogState: SettingsDialogState) {
    if (dialogState !is SettingsDialogState.ChangeServerBaseUrl) {
      cancelDraftServerAvailabilityCheck()
    }

    _runtimeUiState.update { state ->
      state.copy(
        dialogState = dialogState
      )
    }
  }

  /**
   * Hides the currently shown settings dialog.
   */
  fun clearDialogState() {
    cancelDraftServerAvailabilityCheck()

    _runtimeUiState.update { state ->
      state.copy(
        dialogState = SettingsDialogState.None
      )
    }
  }

  /**
   * Shows reset data confirmation dialog.
   */
  fun showResetDataDialog() {
    setDialogState(
      SettingsDialogState.ResetData()
    )
  }

  /**
   * Shows server base URL edit dialog using the currently persisted URL as initial draft.
   */
  fun showChangeServerBaseUrlDialog() {
    val currentServerBaseUrl = currentSavedServerBaseUrl()

    setDialogState(
      SettingsDialogState.ChangeServerBaseUrl(
        draftServerBaseUrl = currentServerBaseUrl,
        draftServerAvailabilityState = currentServerAvailabilityState(
          serverBaseUrl = currentServerBaseUrl
        )
      )
    )
  }

  /**
   * Updates reset data confirmation text.
   */
  fun setResetDataConfirmationText(value: String) {
    updateDialogState<SettingsDialogState.ResetData> { dialogState ->
      dialogState.copy(
        confirmationText = value
      )
    }
  }

  /**
   * Clears reset data confirmation text.
   */
  fun clearResetDataConfirmationText() {
    setResetDataConfirmationText("")
  }

  /**
   * Updates server base URL draft inside the edit dialog.
   */
  fun setDraftServerBaseUrl(value: String) {
    cancelDraftServerAvailabilityCheck()

    val draftAvailabilityState = if (
      normalizeServerBaseUrl(value) == currentSavedServerBaseUrl()
    ) {
      currentServerAvailabilityState(
        serverBaseUrl = value
      )
    } else {
      ServerAvailabilityState.Unknown
    }

    updateDialogState<SettingsDialogState.ChangeServerBaseUrl> { dialogState ->
      dialogState.copy(
        draftServerBaseUrl = value,
        draftServerAvailabilityState = draftAvailabilityState
      )
    }
  }

  /**
   * Clears server base URL draft inside the edit dialog.
   */
  fun clearDraftServerBaseUrl() {
    setDraftServerBaseUrl("")
  }

  // -----------------------------------------------------------------------------------------------
  // Server availability actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Checks availability of the currently persisted server base URL.
   */
  fun checkSavedServerBaseUrl() {
    checkSavedServerBaseUrl(
      serverBaseUrl = currentSavedServerBaseUrl()
    )
  }

  /**
   * Checks availability of the currently edited server base URL draft.
   */
  fun checkDraftServerBaseUrl() {
    val dialogState = runtimeUiState.value.dialogState

    if (dialogState !is SettingsDialogState.ChangeServerBaseUrl) {
      return
    }

    checkServerBaseUrlAvailability(
      serverBaseUrl = dialogState.draftServerBaseUrl,
      availabilityCheck = draftServerAvailabilityCheck,
      setAvailabilityState = ::setDraftServerAvailabilityState,
      isResultStillRelevant = { checkedUrl ->
        val currentDialogState = runtimeUiState.value.dialogState

        currentDialogState is SettingsDialogState.ChangeServerBaseUrl &&
          normalizeServerBaseUrl(currentDialogState.draftServerBaseUrl) == checkedUrl
      }
    )
  }

  /**
   * Checks availability of the provided saved server base URL.
   */
  private fun checkSavedServerBaseUrl(serverBaseUrl: String) {
    markSavedServerAvailabilityCheckAttempt()

    checkServerBaseUrlAvailability(
      serverBaseUrl = serverBaseUrl,
      availabilityCheck = savedServerAvailabilityCheck,
      setAvailabilityState = ::setSavedServerAvailabilityState,
      isResultStillRelevant = { checkedUrl ->
        savedServerAvailabilityCheck.url == checkedUrl ||
          currentSavedServerBaseUrl() == checkedUrl
      }
    )
  }

  /**
   * Common server availability check pipeline.
   *
   * Validates URL format, cancels obsolete checks, prevents duplicate checks for the same URL,
   * updates loading state, and ignores stale results when the checked URL is no longer relevant.
   */
  private fun checkServerBaseUrlAvailability(
    serverBaseUrl: String,
    availabilityCheck: ServerAvailabilityCheck,
    setAvailabilityState: (ServerAvailabilityState) -> Unit,
    isResultStillRelevant: (String) -> Boolean
  ) {
    val normalizedBaseUrl = normalizeServerBaseUrl(serverBaseUrl)

    if (!prepareServerAvailabilityCheck(
        serverBaseUrl = normalizedBaseUrl,
        availabilityCheck = availabilityCheck,
        setAvailabilityState = setAvailabilityState
      )
    ) {
      return
    }

    val checkJob = viewModelScope.launch(
      start = CoroutineStart.LAZY
    ) {
      val availabilityState = checkServerBaseUrlSafely(
        serverBaseUrl = normalizedBaseUrl
      )

      if (availabilityCheck.url != normalizedBaseUrl) {
        return@launch
      }

      val isRelevant = isResultStillRelevant(normalizedBaseUrl)

      availabilityCheck.clear()

      if (!isRelevant) {
        return@launch
      }

      setAvailabilityState(availabilityState)
    }

    availabilityCheck.start(
      serverBaseUrl = normalizedBaseUrl,
      job = checkJob
    )

    checkJob.start()
  }

  /**
   * Checks availability of the currently persisted server base URL only if the previous check is stale.
   *
   * This is useful for screen-entry checks because it survives configuration changes through ViewModel.
   */
  fun checkSavedServerBaseUrlIfStale() {
    if (isSavedServerAvailabilityCheckFresh()) {
      return
    }

    checkSavedServerBaseUrl()
  }

  // -----------------------------------------------------------------------------------------------
  // Settings actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Updates the selected theme mode.
   */
  fun setThemeMode(themeMode: ThemeMode) {
    viewModelScope.launch {
      settingsRepository.setThemeMode(themeMode)
    }
  }

  /**
   * Updates the selected language code.
   */
  fun setLanguageCode(languageCode: String) {
    viewModelScope.launch {
      settingsRepository.setLanguageCode(languageCode)
    }
  }

  /**
   * Updates inspection backend server base URL.
   */
  fun setServerBaseUrl(serverBaseUrl: String) {
    val normalizedBaseUrl = normalizeValidServerBaseUrlOrNull(
      serverBaseUrl = serverBaseUrl,
      setInvalidAvailabilityState = ::setSavedServerAvailabilityState
    ) ?: return

    saveServerBaseUrlAndCheck(
      serverBaseUrl = normalizedBaseUrl
    )
  }

  /**
   * Saves current server base URL draft from the edit dialog.
   */
  fun saveDraftServerBaseUrl() {
    val dialogState = runtimeUiState.value.dialogState

    if (dialogState !is SettingsDialogState.ChangeServerBaseUrl) {
      return
    }

    val draftServerBaseUrl = normalizeValidServerBaseUrlOrNull(
      serverBaseUrl = dialogState.draftServerBaseUrl,
      setInvalidAvailabilityState = ::setDraftServerAvailabilityState
    ) ?: return

    saveServerBaseUrlAndCheck(
      serverBaseUrl = draftServerBaseUrl
    )
  }

  /**
   * Clears inspection backend server base URL.
   */
  fun clearServerBaseUrl() {
    viewModelScope.launch {
      settingsRepository.setServerBaseUrl(AppSettings.DEFAULT_SERVER_BASE_URL)

      clearAppliedServerBaseUrl(
        availabilityState = ServerAvailabilityState.Unknown
      )

      clearDialogState()
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Inspection reports and data actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Imports reports by inserting them into the database.
   *
   * @return Ids of inserted reports in the same order as [inspectionReports].
   */
  suspend fun importInspectionReports(
    inspectionReports: List<InspectionReport>
  ): List<Long> {
    if (inspectionReports.isEmpty()) {
      return emptyList()
    }

    return inspectionReportRepository.insertAll(
      inspectionReports = inspectionReports
    )
  }

  /**
   * Resets reports to default seed data if the confirmation word is entered correctly.
   */
  fun resetInspectionReportsToDefaults() {
    val dialogState = runtimeUiState.value.dialogState

    if (dialogState !is SettingsDialogState.ResetData) {
      return
    }

    if (!dialogState.isConfirmationValid) {
      return
    }

    viewModelScope.launch {
      inspectionReportRepository.resetToDefaults()
      clearDialogState()
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Settings initialization and synchronization
  // -----------------------------------------------------------------------------------------------

  private fun ensureDefaultSettings() {
    viewModelScope.launch {
      settingsRepository.ensureDefaults()
    }
  }

  private fun observeServerBaseUrl() {
    viewModelScope.launch {
      settings
        .map { appSettings -> appSettings.serverBaseUrl }
        .distinctUntilChanged()
        .collect { serverBaseUrl ->
          val normalizedBaseUrl = normalizeServerBaseUrl(serverBaseUrl)

          applyServerBaseUrl(normalizedBaseUrl)

          if (
            normalizedBaseUrl.isNotBlank() &&
            !hasCheckedInitialNonBlankServerBaseUrl
          ) {
            hasCheckedInitialNonBlankServerBaseUrl = true

            checkSavedServerBaseUrl(
              serverBaseUrl = normalizedBaseUrl
            )
          }
        }
    }
  }

  private fun applyServerBaseUrl(serverBaseUrl: String) {
    val normalizedBaseUrl = normalizeServerBaseUrl(serverBaseUrl)

    if (normalizedBaseUrl.isBlank()) {
      clearAppliedServerBaseUrl(
        availabilityState = ServerAvailabilityState.Unknown
      )
      return
    }

    if (!inspectionProvider.isValidBaseUrl(normalizedBaseUrl)) {
      clearAppliedServerBaseUrl(
        availabilityState = ServerAvailabilityState.Unavailable
      )
      return
    }

    resetSavedAvailabilityForNewAppliedBaseUrl(
      serverBaseUrl = normalizedBaseUrl
    )

    runCatching {
      inspectionProvider.updateBaseUrl(normalizedBaseUrl)
      appliedServerBaseUrl = normalizedBaseUrl
    }.onFailure {
      clearAppliedServerBaseUrl(
        availabilityState = ServerAvailabilityState.Unavailable
      )
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Server base URL normalization helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns the currently persisted server base URL in normalized form.
   */
  private fun currentSavedServerBaseUrl(): String {
    return normalizeServerBaseUrl(
      settings.value.serverBaseUrl
    )
  }

  /**
   * Normalizes a server base URL entered by user or loaded from settings.
   */
  private fun normalizeServerBaseUrl(serverBaseUrl: String): String {
    return serverBaseUrl.trim()
  }

  /**
   * Returns normalized server base URL if it is blank or valid.
   *
   * Blank URL is allowed because it means that backend API is not configured.
   * Invalid non-blank URL changes the provided availability state and returns null.
   */
  private fun normalizeValidServerBaseUrlOrNull(
    serverBaseUrl: String,
    setInvalidAvailabilityState: (ServerAvailabilityState) -> Unit
  ): String? {
    val normalizedBaseUrl = normalizeServerBaseUrl(serverBaseUrl)

    if (
      normalizedBaseUrl.isNotBlank() &&
      !inspectionProvider.isValidBaseUrl(normalizedBaseUrl)
    ) {
      setInvalidAvailabilityState(ServerAvailabilityState.Unavailable)
      return null
    }

    return normalizedBaseUrl
  }

  // -----------------------------------------------------------------------------------------------
  // Server availability state helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns availability state that should be shown for the provided server base URL.
   *
   * Blank URLs are always treated as unknown.
   * Saved Checking state is not copied into draft dialog because it is transient and belongs
   * to the saved URL card check lifecycle.
   */
  private fun currentServerAvailabilityState(
    serverBaseUrl: String
  ): ServerAvailabilityState {
    val normalizedBaseUrl = normalizeServerBaseUrl(serverBaseUrl)

    if (normalizedBaseUrl.isBlank()) {
      return ServerAvailabilityState.Unknown
    }

    val savedAvailabilityState = runtimeUiState.value.savedServerAvailabilityState

    return if (savedAvailabilityState == ServerAvailabilityState.Checking) {
      ServerAvailabilityState.Unknown
    } else {
      savedAvailabilityState
    }
  }

  /**
   * Converts server health check result to UI availability state.
   */
  private fun Boolean.toServerAvailabilityState(): ServerAvailabilityState {
    return if (this) {
      ServerAvailabilityState.Available
    } else {
      ServerAvailabilityState.Unavailable
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Server availability check lifecycle helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Prepares server availability check for the provided URL.
   *
   * Returns false when the check should not be started:
   * - URL is blank;
   * - URL is invalid;
   * - the same URL is already being checked.
   *
   * Cancels obsolete checks and updates UI availability state when needed.
   */
  private fun prepareServerAvailabilityCheck(
    serverBaseUrl: String,
    availabilityCheck: ServerAvailabilityCheck,
    setAvailabilityState: (ServerAvailabilityState) -> Unit
  ): Boolean {
    if (serverBaseUrl.isBlank()) {
      availabilityCheck.cancel()
      setAvailabilityState(ServerAvailabilityState.Unknown)
      return false
    }

    if (!inspectionProvider.isValidBaseUrl(serverBaseUrl)) {
      availabilityCheck.cancel()
      setAvailabilityState(ServerAvailabilityState.Unavailable)
      return false
    }

    if (availabilityCheck.isActiveFor(serverBaseUrl)) {
      return false
    }

    availabilityCheck.cancel()
    setAvailabilityState(ServerAvailabilityState.Checking)

    return true
  }

  /**
   * Checks server health and converts any failure to Unavailable state.
   */
  private suspend fun checkServerBaseUrlSafely(
    serverBaseUrl: String
  ): ServerAvailabilityState {
    return runCatching {
      inspectionProvider.checkServerHealth(
        baseUrl = serverBaseUrl
      ).getOrDefault(false).toServerAvailabilityState()
    }.getOrDefault(ServerAvailabilityState.Unavailable)
  }

  /**
   * Cancels current saved server availability check.
   */
  private fun cancelSavedServerAvailabilityCheck() {
    savedServerAvailabilityCheck.cancel()
  }

  /**
   * Cancels current draft server availability check.
   */
  private fun cancelDraftServerAvailabilityCheck() {
    draftServerAvailabilityCheck.cancel()
  }

  /**
   * Returns whether saved server availability check was performed recently enough.
   */
  private fun isSavedServerAvailabilityCheckFresh(): Boolean {
    val lastCheckTime = lastSavedServerAvailabilityCheckElapsedRealtime

    if (lastCheckTime == 0L) {
      return false
    }

    return currentElapsedRealtime() - lastCheckTime <
      SavedServerAvailabilityAutoCheckMinIntervalMillis
  }

  /**
   * Marks current time as the latest saved server availability check attempt.
   */
  private fun markSavedServerAvailabilityCheckAttempt() {
    lastSavedServerAvailabilityCheckElapsedRealtime = currentElapsedRealtime()
  }

  /**
   * Returns monotonic elapsed realtime for check throttling.
   */
  private fun currentElapsedRealtime(): Long {
    return SystemClock.elapsedRealtime()
  }

  // -----------------------------------------------------------------------------------------------
  // Server base URL persistence helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Saves server base URL to settings and starts availability check for the saved value.
   */
  private fun saveServerBaseUrlAndCheck(
    serverBaseUrl: String
  ) {
    viewModelScope.launch {
      settingsRepository.setServerBaseUrl(serverBaseUrl)
      clearDialogState()

      checkSavedServerBaseUrl(
        serverBaseUrl = serverBaseUrl
      )
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Applied server base URL helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Resets saved availability state when a new server base URL is applied.
   *
   * Does not reset state if this exact URL is already being checked.
   */
  private fun resetSavedAvailabilityForNewAppliedBaseUrl(
    serverBaseUrl: String
  ) {
    val isNewAppliedBaseUrl = appliedServerBaseUrl != serverBaseUrl

    if (
      isNewAppliedBaseUrl &&
      savedServerAvailabilityCheck.url != serverBaseUrl
    ) {
      cancelSavedServerAvailabilityCheck()
      setSavedServerAvailabilityState(ServerAvailabilityState.Unknown)
    }
  }

  /**
   * Clears currently applied backend base URL and updates saved availability state.
   */
  private fun clearAppliedServerBaseUrl(
    availabilityState: ServerAvailabilityState
  ) {
    appliedServerBaseUrl = null
    cancelSavedServerAvailabilityCheck()
    inspectionProvider.clearBaseUrl()
    setSavedServerAvailabilityState(availabilityState)
  }

  // -----------------------------------------------------------------------------------------------
  // Runtime state helpers
  // -----------------------------------------------------------------------------------------------

  private fun setSavedServerAvailabilityState(
    availabilityState: ServerAvailabilityState
  ) {
    _runtimeUiState.update { state ->
      state.copy(
        savedServerAvailabilityState = availabilityState,
        dialogState = state.dialogState.syncDraftServerAvailabilityWithSaved(
          savedServerBaseUrl = currentSavedServerBaseUrl(),
          savedAvailabilityState = availabilityState
        )
      )
    }
  }

  private fun setDraftServerAvailabilityState(
    availabilityState: ServerAvailabilityState
  ) {
    updateDialogState<SettingsDialogState.ChangeServerBaseUrl> { dialogState ->
      dialogState.copy(
        draftServerAvailabilityState = availabilityState
      )
    }
  }

  private inline fun <reified T : SettingsDialogState> updateDialogState(
    crossinline transform: (T) -> SettingsDialogState
  ) {
    _runtimeUiState.update { state ->
      val dialogState = state.dialogState

      if (dialogState is T) {
        state.copy(
          dialogState = transform(dialogState)
        )
      } else {
        state
      }
    }
  }

  private val SettingsDialogState.ResetData.isConfirmationValid: Boolean
    get() = confirmationText.trim() == RESET_DATA_CONFIRMATION_WORD

  /**
   * Synchronizes opened server base URL dialog with saved availability result.
   *
   * This updates draft availability only when:
   * - change server dialog is opened;
   * - saved check is finished, not Checking;
   * - draft check is not running;
   * - draft URL is still the same as saved URL.
   */
  private fun SettingsDialogState.syncDraftServerAvailabilityWithSaved(
    savedServerBaseUrl: String,
    savedAvailabilityState: ServerAvailabilityState
  ): SettingsDialogState {
    if (this !is SettingsDialogState.ChangeServerBaseUrl) {
      return this
    }

    if (savedAvailabilityState == ServerAvailabilityState.Checking) {
      return this
    }

    if (draftServerAvailabilityCheck.url != null) {
      return this
    }

    val normalizedDraftServerBaseUrl = normalizeServerBaseUrl(
      draftServerBaseUrl
    )

    if (normalizedDraftServerBaseUrl != savedServerBaseUrl) {
      return this
    }

    return copy(
      draftServerAvailabilityState = if (normalizedDraftServerBaseUrl.isBlank()) {
        ServerAvailabilityState.Unknown
      } else {
        savedAvailabilityState
      }
    )
  }

  // -----------------------------------------------------------------------------------------------
  // Companion
  // -----------------------------------------------------------------------------------------------

  companion object {

    /**
     * Creates a factory for [SettingsViewModel].
     */
    fun factory(
      settingsRepository: SettingsRepository,
      inspectionReportRepository: InspectionReportRepository,
      inspectionProvider: InspectionProvider
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        SettingsViewModel(
          settingsRepository = settingsRepository,
          inspectionReportRepository = inspectionReportRepository,
          inspectionProvider = inspectionProvider
        )
      }
    }
  }
}