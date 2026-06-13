package com.karandaev.geo_inspect.core.presentation.auth

import android.content.Context
import android.net.Uri
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.karandaev.geo_inspect.core.auth.firebase.FirebaseAuthAccountManager
import com.karandaev.geo_inspect.core.auth.firebase.FirebaseGoogleAuthProvider
import com.karandaev.geo_inspect.core.auth.firebase.toUserProfile
import com.karandaev.geo_inspect.core.domain.model.persisted.AppAccessState
import com.karandaev.geo_inspect.core.domain.repository.PersistedAppStateRepository
import com.karandaev.geo_inspect.core.image.crop.ImageCropper
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFileProvider
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthDialogState
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthSession
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthUiState
import com.karandaev.geo_inspect.core.presentation.auth.validation.AuthValidation
import com.karandaev.geo_inspect.core.presentation.auth.validation.PasswordStrengthCalculator
import com.karandaev.geo_inspect.core.inspection_api.InspectionProvider
import com.karandaev.geo_inspect.core.presentation.auth.model.PendingSensitiveOperation
import com.karandaev.geo_inspect.core.presentation.message.UiMessage
import com.karandaev.geo_inspect.core.presentation.message.UiMessageDisplayMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DELETE_ACCOUNT_CONFIRMATION_WORD = "DELETE"
private const val CurrentUserAvatarAutoRefreshMinIntervalMillis = 60_000L

/**
 * ViewModel that manages Firebase authentication and app access state.
 *
 * Authentication is backed by Firebase Auth.
 * Google sign-in is delegated to [FirebaseGoogleAuthProvider].
 * Guest access is persisted through [PersistedAppStateRepository].
 *
 * @param auth Shared Firebase authentication instance.
 * @param authAccountManager Manager for firebase auth actions.
 * @param persistedAppStateRepository Repository used to persist guest access state.
 */
class AuthViewModel(
  private val auth: FirebaseAuth,
  private val authAccountManager: FirebaseAuthAccountManager,
  private val persistedAppStateRepository: PersistedAppStateRepository,
  private val inspectionProvider: InspectionProvider,
  private val imageSourceFileProvider: ImageSourceFileProvider,
  private val imageCropper: ImageCropper
) : ViewModel() {

  // -----------------------------------------------------------------------------------------------
  // State
  // -----------------------------------------------------------------------------------------------

  private val _state = MutableStateFlow(
    AuthUiState(
      session = auth.toAuthSession()
    )
  )

  /**
   * Current immutable UI state.
   */
  val state: StateFlow<AuthUiState> = _state.asStateFlow()

  private var nextRefreshId = 0L

  private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
    val newSession = firebaseAuth.toAuthSession()

    _state.update { state ->
      state.copy(
        session = newSession
      )
    }

    if (newSession is AuthSession.SignedIn) {
      refreshCurrentUserAvatarUrlSilently()
    }
  }

  private var lastCurrentUserAvatarRefreshElapsedRealtime = 0L
  private var currentUserAvatarRefreshJob: Job? = null

  private var pendingSensitiveOperation: PendingSensitiveOperation? = null

  // -----------------------------------------------------------------------------------------------
  // Lifecycle
  // -----------------------------------------------------------------------------------------------

  init {
    auth.addAuthStateListener(authListener)
    observeAppAccessState()
  }

  override fun onCleared() {
    auth.removeAuthStateListener(authListener)
    super.onCleared()
  }

  // -----------------------------------------------------------------------------------------------
  // Guest access
  // -----------------------------------------------------------------------------------------------

  /**
   * Continues into the app without signing in.
   *
   * Guest access is persisted between app launches.
   */
  fun continueAsGuest() {
    viewModelScope.launch {
      persistedAppStateRepository.setAppAccessState(
        createAppAccessState(
          isGuestModeEnabled = true
        )
      )

      showInfo(
        text = "Continuing as guest.",
        displayMode = UiMessageDisplayMode.Toast
      )

      clearCredentials()
      clearDialogState()
    }
  }

  /**
   * Clears guest mode only in the current in-memory UI state.
   *
   * This is useful when the app must leave guest access immediately without changing
   * the persisted guest-mode choice.
   */
  fun clearGuestModeSilently() {
    _state.update { state ->
      state.copy(
        isGuestModeEnabled = false
      )
    }
  }

  // -----------------------------------------------------------------------------------------------
  // UI message actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Clears the current one-shot UI message.
   *
   * This does not change the current signed-in or signed-out session.
   */
  fun clearMessage() {
    _state.update { state ->
      state.copy(message = null)
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Input setters
  // -----------------------------------------------------------------------------------------------

  /**
   * Updates the user name input value.
   */
  fun setUserName(value: String) {
    _state.update { state ->
      state.copy(userName = value)
    }
  }

  /**
   * Updates the email input value.
   */
  fun setEmail(value: String) {
    _state.update { state ->
      state.copy(email = value)
    }
  }

  /**
   * Updates the password input value.
   */
  fun setPassword(value: String) {
    _state.update { state ->
      state.copy(
        password = value,
        passwordStrength = PasswordStrengthCalculator.calculate(value)
      )
    }
  }

  /**
   * Updates the repeated password input value.
   */
  fun setRepeatedPassword(value: String) {
    _state.update { state ->
      state.copy(repeatedPassword = value)
    }
  }

  /**
   * Updates the original password input value used for reauthentication.
   */
  fun setOriginalPassword(value: String) {
    _state.update { state ->
      state.copy(
        originalPassword = value
      )
    }
  }

  /**
   * Updates delete account confirmation text.
   */
  fun setDeleteAccountConfirmationText(value: String) {
    _state.update { state ->
      state.copy(
        deleteAccountConfirmationText = value
      )
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Input clear actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Clears the user name input value.
   */
  fun clearUserName() {
    _state.update { state ->
      state.copy(userName = "")
    }
  }

  /**
   * Clears the email input value.
   */
  fun clearEmail() {
    _state.update { state ->
      state.copy(email = "")
    }
  }

  /**
   * Clears password input values.
   */
  fun clearPassword() {
    _state.update { state ->
      state.copy(
        password = "",
        repeatedPassword = "",
        passwordStrength = 0
      )
    }
  }

  /**
   * Clears the repeated password input value.
   */
  fun clearRepeatedPassword() {
    _state.update { state ->
      state.copy(repeatedPassword = "")
    }
  }

  /**
   * Clears the original password input value used for reauthentication.
   */
  fun clearOriginalPassword() {
    _state.update { state ->
      state.copy(
        originalPassword = ""
      )
    }
  }

  /**
   * Clears delete account confirmation text.
   */
  fun clearDeleteAccountConfirmationText() {
    _state.update { state ->
      state.copy(
        deleteAccountConfirmationText = ""
      )
    }
  }

  /**
   * Clears user name, email and password input values.
   */
  fun clearCredentials() {
    clearReauthenticationRequest()

    _state.update { state ->
      state.copy(
        userName = "",
        email = "",
        password = "",
        repeatedPassword = "",
        deleteAccountConfirmationText = "",
        passwordStrength = 0,
        avatarImageSource = null
      )
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Authentication actions
  // -----------------------------------------------------------------------------------------------

  fun register(
    onSuccess: () -> Unit = {}
  ) = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        showError(AuthErrorMapper.toMessage(error))
      }
    ) {
      val userName = _state.value.userName.trim()
      val email = AuthValidation.requireValidEmail(_state.value.email)
      val password = _state.value.password
      val repeatedPassword = _state.value.repeatedPassword
      val passwordStrength = _state.value.passwordStrength

      require(userName.isNotBlank()) {
        "Please enter your name."
      }

      AuthValidation.requireStrongRepeatedPassword(
        password = password,
        repeatedPassword = repeatedPassword,
        passwordStrength = passwordStrength
      )

      authAccountManager.registerWithEmail(
        userName = userName,
        email = email,
        password = password
      )

      showInfo(
        text = "Verification email sent to $email. Please verify your email, then sign in.",
        displayMode = UiMessageDisplayMode.Toast
      )

      clearPassword()
      clearUserName()
      clearProfileAvatarImage()
      onSuccess()
    }
  }

  /**
   * Signs in a user with email and password.
   *
   * If the email is not verified, a new verification email is sent and the user
   * is signed out.
   */
  fun login() = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        showError(AuthErrorMapper.toMessage(error))
      }
    ) {
      val email = AuthValidation.requireValidEmail(_state.value.email)
      val password = _state.value.password

      require(password.isNotBlank()) {
        "Please enter your password."
      }

      val isEmailVerified = authAccountManager.loginWithEmail(
        email = email,
        password = password
      )

      if (!isEmailVerified) {
        showInfo(
          "Your email is not verified yet. We sent a verification email to $email. Please verify it and try again."
        )

        return@runWithLoading
      }

      showInfo(
        text = "Signed in successfully.",
        displayMode = UiMessageDisplayMode.Toast
      )

      clearGuestMode()
      clearUserName()
      clearPassword()
      clearProfileAvatarImage()
    }
  }

  /**
   * Sends a password reset email to the current email input value.
   */
  fun sendPasswordResetEmail(
    onSuccess: () -> Unit = {}
  ) = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        showError(AuthErrorMapper.toMessage(error))
      }
    ) {
      val email = AuthValidation.requireValidEmail(
        value = _state.value.email,
        blankMessage = "Please enter your email address first."
      )

      authAccountManager.sendPasswordResetEmail(email)

      showInfo(
        text = "Password reset email sent to $email. Please check your inbox.",
        displayMode = UiMessageDisplayMode.Toast
      )
      onSuccess()
    }
  }

  /**
   * Signs in the user with Google using Credential Manager and Firebase Auth.
   *
   * @param context Android context required by Credential Manager.
   */
  fun signInWithGoogle(context: Context) = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        showError(AuthErrorMapper.toMessage(error))
      }
    ) {
      authAccountManager.signInWithGoogle(context)
      showInfo(
        text = "Signed in with Google successfully.",
        displayMode = UiMessageDisplayMode.Toast
      )

      clearGuestMode()
      clearCredentials()
    }
  }

  /**
   * Signs out from Firebase.
   *
   * This does not enable guest mode automatically. If the user wants to continue
   * without account after logout, they should explicitly choose guest access again.
   */
  fun logout() = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        forceLocalLogout()
        showError(
          text = "Signed out, but something went wrong. ${error.message.orEmpty()}".trim(),
          displayMode = UiMessageDisplayMode.Toast
        )
      }
    ) {
      forceLocalLogout()

      showInfo(
        text = "You have been signed out.",
        displayMode = UiMessageDisplayMode.Toast
      )
    }
  }


  // -----------------------------------------------------------------------------------------------
  // Profile avatar image source actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Updates selected profile avatar image source.
   *
   * The selected URI is copied into a local source file immediately.
   *
   * @param context Android context used to copy selected URI into cache.
   * @param uri URI of the selected avatar image, or null to clear selection.
   */
  fun setProfileAvatarImage(
    context: Context,
    uri: Uri?
  ) {
    _state.update { state ->
      if (uri == null) {
        state.copy(avatarImageSource = null)
      } else {
        state.copy(
          avatarImageSource = imageSourceFileProvider.createSourceFile(
            context = context,
            uri = uri
          )
        )
      }
    }
  }

  /**
   * Applies crop to the currently selected profile avatar image.
   *
   * Crop coordinates must be normalized relative to the original image.
   *
   * @param context Android context used to create cropped file in cache.
   * @param cropBox Normalized crop box, or null to reset crop.
   */
  fun setProfileAvatarCropBox(
    context: Context,
    cropBox: NormalizedCropBox?
  ) {
    val currentImageSource = _state.value.avatarImageSource ?: return

    val updatedImageSource = if (cropBox == null) {
      currentImageSource.copy(
        sourcePath = currentImageSource.originalSourcePath,
        cropBox = null
      )
    } else {
      imageCropper.crop(
        context = context,
        imageSource = currentImageSource,
        cropBox = cropBox
      )
    }

    _state.update { state ->
      state.copy(
        avatarImageSource = updatedImageSource
      )
    }
  }

  /**
   * Updates selected profile avatar crop box draft.
   *
   * The image is not cropped immediately because this function may be called
   * very often while the user moves the cropper with fingers.
   */
  fun setSilentlyProfileAvatarCropBox(
    cropBox: NormalizedCropBox?
  ) {
    _state.update { state ->
      val imageSource = state.avatarImageSource ?: return@update state

      state.copy(
        avatarImageSource = imageSource.copy(
          sourcePath = imageSource.originalSourcePath,
          cropBox = cropBox
        )
      )
    }
  }

  /**
   * Clears selected profile avatar image source and crop.
   */
  fun clearProfileAvatarImage() {
    _state.update { state ->
      state.copy(
        avatarImageSource = null
      )
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Current user profile actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Updates the current signed-in user's display name.
   */
  fun updateCurrentUserName(
    onSuccess: () -> Unit = {}
  ) = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        showError(AuthErrorMapper.toMessage(error))
      }
    ) {
      val userName = _state.value.userName.trim()

      require(userName.isNotBlank()) {
        "Please enter your name."
      }

      authAccountManager.updateCurrentUserName(userName)
      refreshAuthSession()

      showInfo(
        text = "Profile name updated.",
        displayMode = UiMessageDisplayMode.Toast
      )

      dismissChangeUserNameDialog()
      onSuccess()
    }
  }

  /**
   * Changes password for users signed in with email/password provider.
   */
  fun changePassword() = viewModelScope.launch {
    runSensitiveOperation(
      pendingOperation = PendingSensitiveOperation.ChangePassword
    ) {
      validateNewPassword()

      authAccountManager.changePassword(_state.value.password)

      showInfo(
        text = "Password updated.",
        displayMode = UiMessageDisplayMode.Toast
      )

      dismissChangePasswordDialog()
    }
  }

  /**
   * Deletes the current signed-in account.
   */
  fun deleteAccount() = viewModelScope.launch {
    runSensitiveOperation(
      pendingOperation = PendingSensitiveOperation.DeleteAccount
    ) {
      validateDeleteAccountConfirmation()

      authAccountManager.deleteAccount()

      clearGuestMode()
      dismissDeleteAccountDialog()
      clearCredentials()

      showInfo(
        text = "Account deleted.",
        displayMode = UiMessageDisplayMode.Toast
      )
    }
  }

  /**
   * Reauthenticates current user and retries pending sensitive operation.
   */
  fun reauthenticateCurrentUser() = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        showError(AuthErrorMapper.toMessage(error))
      }
    ) {
      val originalPassword = _state.value.originalPassword.trim()

      require(originalPassword.isNotBlank()) {
        "Please enter your current password."
      }

      authAccountManager.reauthenticateWithPassword(originalPassword)

      val operation = pendingSensitiveOperation
        ?: return@runWithLoading

      clearReauthenticationRequest()
      retrySensitiveOperation(operation)
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Profile avatar actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Uploads selected profile avatar image through backend API and updates local profile photoUrl.
   */
  fun updateCurrentUserAvatar(
    context: Context,
    onSuccess: () -> Unit = {}
  ) = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        showError(AuthErrorMapper.toMessage(error))
      }
    ) {
      val imageSource = _state.value.avatarImageSource

      require(imageSource != null) {
        "Please select an avatar image first."
      }

      val uploadImageSource = imageSource.prepareAvatarImageForUpload(
        context = context
      )

      val avatarUrl = inspectionProvider.updateAvatar(
        sourcePath = uploadImageSource.sourcePath
      ).getOrThrow()

      updateCurrentProfilePhotoUrlWithFallback(avatarUrl)

      dismissChangeAvatarDialog()

      showInfo(
        text = "Profile avatar updated.",
        displayMode = UiMessageDisplayMode.Toast
      )

      onSuccess()
    }
  }

  /**
   * Loads current profile avatar URL from backend API and updates local profile photoUrl.
   */
  fun refreshCurrentUserAvatarUrl() {
    startCurrentUserAvatarRefresh(
      respectFreshness = false,
      showLoading = true,
      showErrors = true,
      countFailedAttempt = true
    )
  }

  /**
   * Silently refreshes backend avatar URL after successful sign-in or profile session refresh.
   *
   * Failed startup attempts are not counted as fresh because backend base URL may not be ready yet.
   */
  private fun refreshCurrentUserAvatarUrlSilently() {
    startCurrentUserAvatarRefresh(
      respectFreshness = false,
      showLoading = false,
      showErrors = false,
      countFailedAttempt = false
    )
  }

  /**
   * Silently refreshes backend avatar URL if previous refresh attempt is stale.
   *
   * This should be called from screens where backend base URL is already expected to be applied.
   */
  fun refreshCurrentUserAvatarUrlIfStale() {
    startCurrentUserAvatarRefresh(
      respectFreshness = true,
      showLoading = false,
      showErrors = false,
      countFailedAttempt = true
    )
  }

  /**
   * Starts profile avatar refresh with common loading, throttling and duplicate-job protection.
   */
  private fun startCurrentUserAvatarRefresh(
    respectFreshness: Boolean,
    showLoading: Boolean,
    showErrors: Boolean,
    countFailedAttempt: Boolean
  ): Job? {
    if (state.value.session !is AuthSession.SignedIn) {
      return null
    }

    if (respectFreshness && isCurrentUserAvatarRefreshFresh()) {
      return null
    }

    currentUserAvatarRefreshJob
      ?.takeIf { job -> job.isActive }
      ?.let { activeJob -> return activeJob }

    if (countFailedAttempt) {
      markCurrentUserAvatarRefreshAttempt()
    }

    val refreshJob = viewModelScope.launch {
      if (showLoading) {
        clearMessage()
        setLoading(true)
      }

      try {
        runCatching {
          loadCurrentUserAvatarUrl()
        }.onSuccess {
          if (!countFailedAttempt) {
            markCurrentUserAvatarRefreshAttempt()
          }
        }.onFailure { error ->
          if (showErrors) {
            showError(AuthErrorMapper.toMessage(error))
          }
        }
      } finally {
        currentUserAvatarRefreshJob = null

        if (showLoading) {
          setLoading(false)
        }
      }
    }

    currentUserAvatarRefreshJob = refreshJob

    return refreshJob
  }

  /**
   * Loads backend avatar URL and applies it to current local signed-in profile.
   */
  private suspend fun loadCurrentUserAvatarUrl() {
    val avatarUrl = inspectionProvider.getAvatarUrl().getOrThrow()

    updateCurrentProfilePhotoUrlWithFallback(avatarUrl)
  }

  /**
   * Resets current user's custom profile avatar through backend API.
   */
  fun resetCurrentUserAvatar(
    onSuccess: () -> Unit = {}
  ) = viewModelScope.launch {
    runWithLoading(
      onFailure = { error ->
        showError(AuthErrorMapper.toMessage(error))
      }
    ) {
      val avatarUrl = inspectionProvider.resetAvatar().getOrThrow()

      updateCurrentProfilePhotoUrlWithFallback(avatarUrl)
      clearProfileAvatarImage()
      clearDialogState()

      showInfo(
        text = "Profile avatar reset.",
        displayMode = UiMessageDisplayMode.Toast
      )

      onSuccess()
    }
  }

  /**
   * Applies selected crop box before avatar upload.
   */
  private fun ImageSourceFile.prepareAvatarImageForUpload(
    context: Context
  ): ImageSourceFile {
    val cropBox = cropBox ?: return this

    return imageCropper.crop(
      context = context,
      imageSource = copy(
        sourcePath = originalSourcePath
      ),
      cropBox = cropBox
    )
  }

  // -----------------------------------------------------------------------------------------------
  // Dialog actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Shows the provided authentication dialog.
   */
  fun setDialogState(dialogState: AuthDialogState) {
    _state.update { state ->
      state.copy(
        dialogState = dialogState
      )
    }
  }

  /**
   * Dismisses display name dialog and clears temporary display name input.
   */
  fun dismissChangeUserNameDialog() {
    clearUserName()
    clearDialogState()
  }

  /**
   * Dismisses change password dialog and clears temporary password inputs.
   */
  fun dismissChangePasswordDialog() {
    clearPassword()
    clearDialogState()
  }

  /**
   * Dismisses avatar dialog and clears selected avatar image.
   */
  fun dismissChangeAvatarDialog() {
    clearProfileAvatarImage()
    clearDialogState()
  }

  /**
   * Dismisses delete account dialog and clears confirmation text.
   */
  fun dismissDeleteAccountDialog() {
    clearDeleteAccountConfirmationText()
    clearDialogState()
  }

  /**
   * Hides the currently shown authentication dialog.
   */
  fun clearDialogState() {
    _state.update { state ->
      state.copy(
        dialogState = AuthDialogState.None
      )
    }
  }

  /**
   * Clears pending reauthentication request and entered original password.
   */
  fun clearReauthenticationRequest() {
    pendingSensitiveOperation = null

    _state.update { state ->
      state.copy(
        originalPassword = "",
        isReauthenticationRequired = false
      )
    }
  }

  /**
   * Cancels pending sensitive operation and clears all temporary sensitive inputs.
   */
  fun cancelSensitiveOperation() {
    pendingSensitiveOperation = null

    _state.update { state ->
      state.copy(
        originalPassword = "",
        password = "",
        repeatedPassword = "",
        deleteAccountConfirmationText = "",
        passwordStrength = 0,
        isReauthenticationRequired = false,
        dialogState = AuthDialogState.None
      )
    }
  }

  fun showChangeUserNameDialog() {
    setDialogState(AuthDialogState.ChangeUserName)
  }

  fun showChangePasswordDialog() {
    setDialogState(AuthDialogState.ChangePassword)
  }

  fun showChangeAvatarDialog() {
    setDialogState(AuthDialogState.ChangeAvatar)
  }

  fun showDeleteAccountDialog() {
    setDialogState(AuthDialogState.DeleteAccount)
  }


  // -----------------------------------------------------------------------------------------------
  // App access state helpers
  // -----------------------------------------------------------------------------------------------

  private fun observeAppAccessState() {
    viewModelScope.launch {
      persistedAppStateRepository.observeAppAccessState()
        .collect { appAccessState ->
          _state.update { state ->
            state.copy(
              isGuestModeEnabled = appAccessState.isGuestModeEnabled
            )
          }
        }
    }
  }

  private suspend fun clearGuestMode() {
    persistedAppStateRepository.setAppAccessState(
      createAppAccessState(
        isGuestModeEnabled = false
      )
    )
  }

  private fun createAppAccessState(
    isGuestModeEnabled: Boolean
  ): AppAccessState {
    return AppAccessState(
      isGuestModeEnabled = isGuestModeEnabled
    )
  }

  // -----------------------------------------------------------------------------------------------
  // Auth session helpers
  // -----------------------------------------------------------------------------------------------

  private suspend fun forceLocalLogout() {
    authAccountManager.signOut()
    clearGuestMode()
    clearUserName()
    clearPassword()
    clearProfileAvatarImage()
    clearDialogState()
  }

  /**
   * Refreshes local auth session after profile changes that may not trigger auth listener.
   */
  private suspend fun refreshAuthSession() {
    authAccountManager.reloadCurrentUser()

    val newSession = auth.toAuthSession()

    _state.update { state ->
      state.copy(
        session = newSession
      )
    }

    if (newSession is AuthSession.SignedIn) {
      refreshCurrentUserAvatarUrlSilently()
    }
  }

  private fun FirebaseAuth.toAuthSession(): AuthSession {
    val user = currentUser

    return if (user == null) {
      AuthSession.SignedOut
    } else {
      AuthSession.SignedIn(
        profile = user.toUserProfile()
      )
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Profile avatar helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Applies backend profile avatar URL to the current signed-in UI profile.
   *
   * If backend has no custom avatar URL, original provider/Firebase photo URL is used as fallback.
   */
  private fun updateCurrentProfilePhotoUrlWithFallback(photoUrl: String?) {
    _state.update { state ->
      val session = state.session

      if (session is AuthSession.SignedIn) {
        state.copy(
          session = session.copy(
            profile = session.profile.copy(
              photoUrl = photoUrl ?: session.profile.originalPhotoUrl
            )
          )
        )
      } else {
        state
      }
    }
  }

  /**
   * Clears current photoUrl inside the signed-in UI profile.
   *
   * This is an explicit local clear and does not apply provider fallback.
   * originalPhotoUrl is preserved.
   */
  private fun clearCurrentProfilePhotoUrl() {
    _state.update { state ->
      val session = state.session

      if (session is AuthSession.SignedIn) {
        state.copy(
          session = session.copy(
            profile = session.profile.copy(
              photoUrl = null
            )
          )
        )
      } else {
        state
      }
    }
  }

  /**
   * Returns whether current user avatar refresh was attempted recently enough.
   */
  private fun isCurrentUserAvatarRefreshFresh(): Boolean {
    val lastRefreshTime = lastCurrentUserAvatarRefreshElapsedRealtime

    if (lastRefreshTime == 0L) {
      return false
    }

    return currentElapsedRealtime() - lastRefreshTime <
      CurrentUserAvatarAutoRefreshMinIntervalMillis
  }

  /**
   * Marks current time as latest current user avatar refresh attempt.
   */
  private fun markCurrentUserAvatarRefreshAttempt() {
    lastCurrentUserAvatarRefreshElapsedRealtime = currentElapsedRealtime()
  }

  /**
   * Returns monotonic elapsed realtime for refresh throttling.
   */
  private fun currentElapsedRealtime(): Long {
    return SystemClock.elapsedRealtime()
  }

  // -----------------------------------------------------------------------------------------------
  // Sensitive operations helpers
  // -----------------------------------------------------------------------------------------------

  private suspend fun runSensitiveOperation(
    pendingOperation: PendingSensitiveOperation,
    block: suspend () -> Unit
  ) {
    runWithLoading(
      onFailure = { error ->
        if (error.isRecentLoginRequired()) {
          requestReauthentication(pendingOperation)
        } else {
          showError(AuthErrorMapper.toMessage(error))
        }
      },
      block = block
    )
  }

  private fun requestReauthentication(
    pendingOperation: PendingSensitiveOperation
  ) {
    pendingSensitiveOperation = pendingOperation

    _state.update { state ->
      state.copy(
        originalPassword = "",
        isReauthenticationRequired = true
      )
    }

    clearDialogState()
  }

  private suspend fun retrySensitiveOperation(
    operation: PendingSensitiveOperation
  ) {
    when (operation) {
      PendingSensitiveOperation.ChangePassword -> {
        validateNewPassword()

        authAccountManager.changePassword(_state.value.password)

        showInfo(
          text = "Password updated.",
          displayMode = UiMessageDisplayMode.Toast
        )

        dismissChangePasswordDialog()
      }

      PendingSensitiveOperation.DeleteAccount -> {
        validateDeleteAccountConfirmation()

        authAccountManager.deleteAccount()

        clearGuestMode()
        dismissDeleteAccountDialog()
        clearCredentials()

        showInfo(
          text = "Account deleted.",
          displayMode = UiMessageDisplayMode.Toast
        )
      }
    }
  }

  /**
   * Checks whether delete account confirmation text matches required word.
   */
  private fun validateDeleteAccountConfirmation() {
    require(
      _state.value.deleteAccountConfirmationText.trim() == DELETE_ACCOUNT_CONFIRMATION_WORD
    ) {
      "Please type $DELETE_ACCOUNT_CONFIRMATION_WORD to confirm account deletion."
    }
  }

  private fun validateNewPassword() {
    AuthValidation.requireStrongRepeatedPassword(
      password = _state.value.password,
      repeatedPassword = _state.value.repeatedPassword,
      passwordStrength = _state.value.passwordStrength,
      passwordBlankMessage = "Please enter your new password.",
      repeatedPasswordBlankMessage = "Please repeat your new password."
    )
  }

  private fun Throwable.isRecentLoginRequired(): Boolean {
    var currentError: Throwable? = this

    while (currentError != null) {
      if (currentError is FirebaseAuthRecentLoginRequiredException) {
        return true
      }

      currentError = currentError.cause
    }

    return false
  }

  // -----------------------------------------------------------------------------------------------
  // Loading and messages
  // -----------------------------------------------------------------------------------------------

  private suspend fun runWithLoading(
    onFailure: suspend (Throwable) -> Unit,
    block: suspend () -> Unit
  ) {
    clearMessage()
    setLoading(true)

    try {
      runCatching {
        block()
      }.onFailure { error ->
        onFailure(error)
      }
    } finally {
      setLoading(false)
    }
  }

  private fun setLoading(isLoading: Boolean) {
    _state.update { state ->
      state.copy(isLoading = isLoading)
    }
  }

  private fun showInfo(
    text: String,
    displayMode: UiMessageDisplayMode = UiMessageDisplayMode.TextBlock
  ) {
    showMessage(
      text = text,
      isError = false,
      displayMode = displayMode
    )
  }

  private fun showError(
    text: String,
    displayMode: UiMessageDisplayMode = UiMessageDisplayMode.TextBlock
  ) {
    showMessage(
      text = text,
      isError = true,
      displayMode = displayMode
    )
  }

  private fun showMessage(
    text: String,
    isError: Boolean,
    displayMode: UiMessageDisplayMode
  ) {
    _state.update { state ->
      state.copy(
        message = UiMessage(
          text = text,
          isError = isError,
          displayMode = displayMode,
          refreshId = nextRefreshId()
        )
      )
    }
  }

  private fun nextRefreshId(): Long {
    nextRefreshId += 1L
    return nextRefreshId
  }

  // -----------------------------------------------------------------------------------------------
  // Companion
  // -----------------------------------------------------------------------------------------------

  companion object
}