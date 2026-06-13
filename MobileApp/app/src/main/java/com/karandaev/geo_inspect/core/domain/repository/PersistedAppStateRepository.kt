package com.karandaev.geo_inspect.core.domain.repository

import com.karandaev.geo_inspect.core.domain.model.persisted.AppAccessState
import com.karandaev.geo_inspect.core.domain.model.persisted.LastKnownLocation
import kotlinx.coroutines.flow.Flow

/**
 * Repository for app runtime state persisted between sessions.
 *
 * This is intentionally separated from user settings.
 */
interface PersistedAppStateRepository {

  /**
   * Observes the last successfully resolved real device location.
   */
  fun observeLastKnownLocation(): Flow<LastKnownLocation?>

  /**
   * Saves or clears the last successfully resolved real device location.
   */
  suspend fun setLastKnownLocation(location: LastKnownLocation?)

  /**
   * Observes persisted app access state.
   *
   * Emits default state when no value has been persisted yet.
   */
  fun observeAppAccessState(): Flow<AppAccessState>

  /**
   * Saves app access state.
   */
  suspend fun setAppAccessState(state: AppAccessState)
}