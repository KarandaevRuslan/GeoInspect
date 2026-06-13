package com.karandaev.geo_inspect.core.domain.repository

import com.karandaev.geo_inspect.core.domain.model.AppSettings
import com.karandaev.geo_inspect.core.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction for app settings.
 *
 * The domain and presentation layers depend on this interface instead of a concrete
 * local database implementation.
 */
interface SettingsRepository {

  /**
   * Observes current app settings.
   */
  fun observeSettings(): Flow<AppSettings>

  /**
   * Ensures that default settings exist in the data source.
   */
  suspend fun ensureDefaults()

  /**
   * Updates the selected theme mode.
   */
  suspend fun setThemeMode(themeMode: ThemeMode)

  /**
   * Updates the selected language code.
   */
  suspend fun setLanguageCode(languageCode: String)

  /**
   * Updates road crack backend server base URL.
   */
  suspend fun setServerBaseUrl(serverBaseUrl: String)
}