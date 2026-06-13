package com.karandaev.geo_inspect.core.presentation.settings

/**
 * Confirmation word required for resetting app data.
 */
const val RESET_DATA_CONFIRMATION_WORD = "RESET"

/**
 * Runtime-only UI state for settings screen.
 *
 * This state is not persisted and is used only for temporary UI state such as dialogs.
 *
 * @param dialogState Currently shown settings dialog.
 * @param savedServerAvailabilityState Availability state of the currently persisted server base URL.
 */
data class SettingsRuntimeUiState(
  val dialogState: SettingsDialogState = SettingsDialogState.None,
  val savedServerAvailabilityState: ServerAvailabilityState = ServerAvailabilityState.Unknown
)

/**
 * UI state of server availability check marker.
 */
enum class ServerAvailabilityState {
  Unknown,
  Checking,
  Available,
  Unavailable
}

/**
 * Runtime-only settings dialog state.
 */
sealed interface SettingsDialogState {

  /**
   * No dialog is currently shown.
   */
  data object None : SettingsDialogState

  /**
   * Reset app data confirmation dialog is currently shown.
   *
   * @param confirmationText Current confirmation text entered by the user.
   */
  data class ResetData(
    val confirmationText: String = ""
  ) : SettingsDialogState

  /**
   * Server base URL edit dialog is currently shown.
   *
   * @param draftServerBaseUrl Current editable server base URL draft.
   * @param draftServerAvailabilityState Availability state of the currently edited server base URL.
   */
  data class ChangeServerBaseUrl(
    val draftServerBaseUrl: String = "",
    val draftServerAvailabilityState: ServerAvailabilityState = ServerAvailabilityState.Unknown
  ) : SettingsDialogState
}