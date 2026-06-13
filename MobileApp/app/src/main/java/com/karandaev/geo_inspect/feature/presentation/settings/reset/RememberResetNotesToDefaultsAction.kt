package com.karandaev.geo_inspect.feature.presentation.settings.reset

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.karandaev.geo_inspect.core.util.other.toast
import kotlinx.coroutines.launch

private const val RESET_SUCCESS_MESSAGE = "Database reset to defaults."
private const val RESET_FAILURE_MESSAGE_PREFIX = "Reset failed"

/**
 * Creates a reusable reset-to-defaults action for the settings screen.
 *
 * The returned lambda starts reset operation, updates loading state, shows
 * user-facing toast messages, and notifies the caller after successful reset.
 *
 * @param resetNotesToDefaults Suspended operation that performs the actual reset.
 * @param onResettingChange Called when reset operation starts or finishes.
 * @param onResetSuccess Called after successful reset.
 * @return Function that starts reset-to-defaults operation.
 */
@Composable
internal fun rememberResetNotesToDefaultsAction(
  resetNotesToDefaults: suspend () -> Unit,
  onResettingChange: (Boolean) -> Unit,
  onResetSuccess: () -> Unit = {}
): () -> Unit {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val currentResetNotesToDefaults by rememberUpdatedState(resetNotesToDefaults)
  val currentOnResettingChange by rememberUpdatedState(onResettingChange)
  val currentOnResetSuccess by rememberUpdatedState(onResetSuccess)

  return remember {
    {
      coroutineScope.launch {
        currentOnResettingChange(true)

        runCatching {
          currentResetNotesToDefaults()
        }.onSuccess {
          context.toast(RESET_SUCCESS_MESSAGE)
          currentOnResetSuccess()
        }.onFailure { error ->
          context.toast(
            "$RESET_FAILURE_MESSAGE_PREFIX: ${error.message ?: "unknown error"}"
          )
        }

        currentOnResettingChange(false)
      }
    }
  }
}