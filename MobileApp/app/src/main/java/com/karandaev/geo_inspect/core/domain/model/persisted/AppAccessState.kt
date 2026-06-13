package com.karandaev.geo_inspect.core.domain.model.persisted

/**
 * Persisted app access state.
 *
 * This state describes how the user accesses the app when there is no active account session.
 * It is intentionally generic so it can be extended later with more access/onboarding flags.
 *
 * @param isGuestModeEnabled Whether the user chose to use the app without signing in.
 */
data class AppAccessState(
  val isGuestModeEnabled: Boolean = false
)