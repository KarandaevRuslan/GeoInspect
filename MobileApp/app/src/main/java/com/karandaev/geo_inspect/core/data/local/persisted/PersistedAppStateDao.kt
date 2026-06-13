package com.karandaev.geo_inspect.core.data.local.persisted

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for universal persisted app state entries.
 */
@Dao
interface PersistedAppStateDao {

  @Query("SELECT * FROM persisted_app_state WHERE `key` = :key")
  fun observeEntry(key: String): Flow<PersistedAppStateEntity?>

  @Query("SELECT * FROM persisted_app_state WHERE `key` = :key")
  suspend fun getEntry(key: String): PersistedAppStateEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsert(entry: PersistedAppStateEntity)

  @Query("DELETE FROM persisted_app_state WHERE `key` = :key")
  suspend fun delete(key: String)
}