package com.karandaev.geo_inspect.core.data.repository

import com.karandaev.geo_inspect.core.data.local.persisted.PersistedAppStateDao
import com.karandaev.geo_inspect.core.data.local.persisted.PersistedAppStateEntity
import com.karandaev.geo_inspect.core.data.local.persisted.PersistedAppStateKeys
import com.karandaev.geo_inspect.core.data.local.persisted.codec.AppAccessStateJsonCodec
import com.karandaev.geo_inspect.core.data.local.persisted.codec.LastKnownLocationJsonCodec
import com.karandaev.geo_inspect.core.domain.model.persisted.AppAccessState
import com.karandaev.geo_inspect.core.domain.model.persisted.LastKnownLocation
import com.karandaev.geo_inspect.core.domain.repository.PersistedAppStateRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local Room-backed implementation of [PersistedAppStateRepository].
 *
 * Stores app runtime state that should survive app restarts but should not be treated as
 * user-configurable settings.
 *
 * Complex values are encoded as JSON strings using Moshi codecs.
 */
class LocalPersistedAppStateRepository(
  private val persistedAppStateDao: PersistedAppStateDao,
  moshi: Moshi,
  private val currentTimeMillis: () -> Long = { System.currentTimeMillis() }
) : PersistedAppStateRepository {

  private val lastKnownLocationCodec = LastKnownLocationJsonCodec(moshi)
  private val appAccessStateCodec = AppAccessStateJsonCodec(moshi)

  /**
   * Observes the last successfully resolved real device location.
   */
  override fun observeLastKnownLocation(): Flow<LastKnownLocation?> {
    return persistedAppStateDao.observeEntry(PersistedAppStateKeys.LAST_KNOWN_LOCATION)
      .map { entity ->
        entity?.value?.let(lastKnownLocationCodec::decodeOrNull)
      }
  }

  /**
   * Saves or clears the last successfully resolved real device location.
   */
  override suspend fun setLastKnownLocation(location: LastKnownLocation?) {
    if (location == null) {
      persistedAppStateDao.delete(PersistedAppStateKeys.LAST_KNOWN_LOCATION)
      return
    }

    upsertEntry(
      key = PersistedAppStateKeys.LAST_KNOWN_LOCATION,
      value = lastKnownLocationCodec.encode(location)
    )
  }

  /**
   * Observes persisted app access state.
   */
  override fun observeAppAccessState(): Flow<AppAccessState> {
    return persistedAppStateDao.observeEntry(PersistedAppStateKeys.APP_ACCESS_STATE)
      .map { entity ->
        entity?.value?.let(appAccessStateCodec::decodeOrNull) ?: AppAccessState()
      }
  }

  /**
   * Saves app access state.
   */
  override suspend fun setAppAccessState(state: AppAccessState) {
    upsertEntry(
      key = PersistedAppStateKeys.APP_ACCESS_STATE,
      value = appAccessStateCodec.encode(state)
    )
  }

  private suspend fun upsertEntry(
    key: String,
    value: String
  ) {
    persistedAppStateDao.upsert(
      PersistedAppStateEntity(
        key = key,
        value = value,
        updatedAt = currentTimeMillis()
      )
    )
  }
}