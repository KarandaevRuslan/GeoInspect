package com.karandaev.geo_inspect.core.presentation.persisted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karandaev.geo_inspect.core.domain.repository.PersistedAppStateRepository
import com.karandaev.geo_inspect.core.location.LocatedPlace
import com.karandaev.geo_inspect.core.presentation.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val PersistedAppStateSharingTimeoutMillis = 5_000L

/**
 * ViewModel for app runtime state persisted between app sessions.
 *
 * This state is separated from user settings because it represents cached app data, not
 * user-configurable preferences.
 */
class PersistedAppStateViewModel(
  private val persistedAppStateRepository: PersistedAppStateRepository
) : ViewModel() {

  /**
   * Last successfully resolved real device location.
   *
   * Emits null when no real location has been cached yet.
   */
  val lastLocatedPlace: StateFlow<LocatedPlace?> =
    persistedAppStateRepository.observeLastKnownLocation()
      .map { lastKnownLocation ->
        lastKnownLocation?.toLocatedPlace()
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(PersistedAppStateSharingTimeoutMillis),
        initialValue = null
      )

  companion object {

    /**
     * Creates a factory for [PersistedAppStateViewModel].
     */
    fun factory(
      persistedAppStateRepository: PersistedAppStateRepository
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        PersistedAppStateViewModel(
          persistedAppStateRepository = persistedAppStateRepository
        )
      }
    }
  }
}