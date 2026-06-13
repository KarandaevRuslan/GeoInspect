package com.karandaev.geo_inspect.core.data.repository

import com.karandaev.geo_inspect.core.data.local.settings.AppSettingsEntity
import com.karandaev.geo_inspect.core.data.local.settings.SettingsDao
import com.karandaev.geo_inspect.core.data.local.settings.toDomain
import com.karandaev.geo_inspect.core.data.local.settings.toEntity
import com.karandaev.geo_inspect.core.domain.model.AppSettings
import com.karandaev.geo_inspect.core.domain.model.ThemeMode
import com.karandaev.geo_inspect.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local Room-backed implementation of [SettingsRepository].
 *
 * This class maps between Room entities and domain models so the rest of the app does not
 * depend on database-specific types.
 */
class LocalSettingsRepository(
  private val settingsDao: SettingsDao
) : SettingsRepository {

  /**
   * Observes app settings from the local database.
   *
   * Emits default settings when the database row does not exist yet.
   */
  override fun observeSettings(): Flow<AppSettings> {
    return settingsDao.observeSettings()
      .map { entity ->
        entity?.toDomain() ?: AppSettings()
      }
  }

  /**
   * Creates default settings when missing.
   */
  override suspend fun ensureDefaults() {
    if (settingsDao.getSettings() == null) {
      settingsDao.upsert(AppSettingsEntity())
    }
  }

  /**
   * Updates the selected theme mode.
   */
  override suspend fun setThemeMode(themeMode: ThemeMode) {
    val current = settingsDao.getSettings()
      ?.toDomain()
      ?: AppSettings()

    settingsDao.upsert(
      current.copy(
        themeMode = themeMode
      ).toEntity()
    )
  }

  /**
   * Updates the selected language code.
   */
  override suspend fun setLanguageCode(languageCode: String) {
    val current = settingsDao.getSettings()
      ?.toDomain()
      ?: AppSettings()

    settingsDao.upsert(
      current.copy(
        languageCode = languageCode
      ).toEntity()
    )
  }

  /**
   * Updates road crack backend server base URL.
   */
  override suspend fun setServerBaseUrl(serverBaseUrl: String) {
    val current = settingsDao.getSettings()
      ?.toDomain()
      ?: AppSettings()

    settingsDao.upsert(
      current.copy(
        serverBaseUrl = serverBaseUrl.trim()
      ).toEntity()
    )
  }
}