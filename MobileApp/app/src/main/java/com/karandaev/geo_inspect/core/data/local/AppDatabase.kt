package com.karandaev.geo_inspect.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.karandaev.geo_inspect.core.data.local.persisted.PersistedAppStateDao
import com.karandaev.geo_inspect.core.data.local.persisted.PersistedAppStateEntity
import com.karandaev.geo_inspect.core.data.local.report.DefaultInspectionReportsSeed
import com.karandaev.geo_inspect.core.data.local.report.DetectionEntity
import com.karandaev.geo_inspect.core.data.local.report.InspectionReportDao
import com.karandaev.geo_inspect.core.data.local.report.InspectionReportEntity
import com.karandaev.geo_inspect.core.data.local.settings.AppSettingsEntity
import com.karandaev.geo_inspect.core.data.local.settings.SettingsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val DatabaseName = "geoinspect_database"

/**
 * Local Room database for GeoInspect.
 *
 * Stores inspection reports, detection results, app settings,
 * and app runtime state persisted between sessions.
 */
@Database(
  entities = [
    InspectionReportEntity::class,
    DetectionEntity::class,
    AppSettingsEntity::class,
    PersistedAppStateEntity::class
  ],
  version = 4,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  /**
   * Returns the DAO for inspection report operations.
   */
  abstract fun inspectionReportDao(): InspectionReportDao

  /**
   * Returns the DAO for settings operations.
   */
  abstract fun settingsDao(): SettingsDao

  /**
   * Returns the DAO for persisted app state operations.
   */
  abstract fun persistedAppStateDao(): PersistedAppStateDao

  companion object {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    /**
     * Returns the singleton database instance.
     */
    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildDatabase(context.applicationContext)
          .also { database -> INSTANCE = database }
      }
    }

    private fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DatabaseName
      )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .addCallback(SeedDatabaseCallback(context))
        .build()
    }
  }

  /**
   * Seeds default data when the database is created for the first time.
   */
  private class SeedDatabaseCallback(
    private val context: Context
  ) : Callback() {

    private val seedScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(db: SupportSQLiteDatabase) {
      super.onCreate(db)

      seedScope.launch {
        val database = getDatabase(context)

        database.inspectionReportDao().insertAll(DefaultInspectionReportsSeed.create())
        database.settingsDao().upsert(AppSettingsEntity())
      }
    }
  }
}