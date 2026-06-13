package com.karandaev.geo_inspect.core.data.local.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing app settings in the local database.
 */
@Dao
interface SettingsDao {

  /**
   * Observes app settings by id.
   */
  @Query("SELECT * FROM app_settings WHERE id = :id")
  fun observeSettings(
    id: Int = AppSettingsEntity.DEFAULT_ID
  ): Flow<AppSettingsEntity?>

  /**
   * Returns app settings by id, or null if settings have not been created yet.
   */
  @Query("SELECT * FROM app_settings WHERE id = :id")
  suspend fun getSettings(
    id: Int = AppSettingsEntity.DEFAULT_ID
  ): AppSettingsEntity?

  /**
   * Inserts or replaces app settings.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsert(settings: AppSettingsEntity)
}