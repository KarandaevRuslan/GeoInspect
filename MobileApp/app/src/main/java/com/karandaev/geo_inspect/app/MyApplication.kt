package com.karandaev.geo_inspect.app

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.karandaev.geo_inspect.core.auth.firebase.FirebaseGoogleAuthProvider
import com.karandaev.geo_inspect.core.data.local.AppDatabase
import com.karandaev.geo_inspect.core.data.repository.LocalInspectionReportRepository
import com.karandaev.geo_inspect.core.data.repository.LocalPersistedAppStateRepository
import com.karandaev.geo_inspect.core.data.repository.LocalSettingsRepository
import com.karandaev.geo_inspect.core.inspection_api.InspectionProvider
import com.karandaev.geo_inspect.core.inspection_api.DefaultInspectionProvider
import com.karandaev.geo_inspect.core.domain.repository.InspectionReportRepository
import com.karandaev.geo_inspect.core.domain.repository.PersistedAppStateRepository
import com.karandaev.geo_inspect.core.domain.repository.SettingsRepository
import com.karandaev.geo_inspect.core.image.cache.ImageCacheCleaner
import com.karandaev.geo_inspect.core.image.crop.ImageCropper
import com.karandaev.geo_inspect.core.image.source_file.DefaultImageSourceFileProvider
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFileProvider
import com.karandaev.geo_inspect.core.json.MoshiFactory
import com.karandaev.geo_inspect.core.location.place.AndroidPlaceNameProvider
import com.karandaev.geo_inspect.core.location.place.PlaceNameProvider
import com.karandaev.geo_inspect.core.location.provider.AndroidLocationProvider
import com.karandaev.geo_inspect.core.location.provider.DeviceLocationProvider
import com.karandaev.geo_inspect.core.location.resolver.CachingLocationResolver
import com.karandaev.geo_inspect.core.location.resolver.DefaultLocationResolver
import com.karandaev.geo_inspect.core.location.resolver.LocationResolver
import com.karandaev.geo_inspect.core.weather.WeatherProvider
import com.karandaev.geo_inspect.core.weather.openmeteo.OpenMeteoWeatherProvider
import com.squareup.moshi.Moshi
import org.osmdroid.config.Configuration
import java.io.File

/**
 * Application class for initializing the database, app services, JSON serialization, and osmdroid.
 * Registered in AndroidManifest.xml using android:name.
 */
class MyApplication : Application() {

  /** Lazy database initialization - created on first access. */
  val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

  /** Shared Moshi instance for JSON serialization. */
  val moshi: Moshi by lazy {
    MoshiFactory.create()
  }

  /** Repository for report operations. */
  val inspectionReportRepository: InspectionReportRepository by lazy {
    LocalInspectionReportRepository(database.inspectionReportDao())
  }

  /** Shared Firebase authentication instance. */
  val firebaseAuth: FirebaseAuth by lazy {
    FirebaseAuth.getInstance()
  }

  /** Provider for signing users into Firebase with Google. */
  val firebaseGoogleAuthProvider: FirebaseGoogleAuthProvider by lazy {
    FirebaseGoogleAuthProvider(
      auth = firebaseAuth
    )
  }

  /** DetectProvider for loading road crack detections from a configurable backend. */
  val inspectionProvider: InspectionProvider by lazy {
    DefaultInspectionProvider(
      auth = firebaseAuth,
      initialBaseUrl = null
    )
  }

  /** Provider for copying selected Android content into local source image files. */
  val imageSourceFileProvider: ImageSourceFileProvider by lazy {
    DefaultImageSourceFileProvider()
  }

  /** Cropper for creating cropped local image files. */
  val imageCropper: ImageCropper by lazy {
    ImageCropper()
  }

  /** Repository for user-configurable app settings. */
  val settingsRepository: SettingsRepository by lazy {
    LocalSettingsRepository(database.settingsDao())
  }

  /** Repository for app runtime state persisted between sessions. */
  val persistedAppStateRepository: PersistedAppStateRepository by lazy {
    LocalPersistedAppStateRepository(
      persistedAppStateDao = database.persistedAppStateDao(),
      moshi = moshi
    )
  }

  /** Provider for converting coordinates to human-readable place names. */
  val placeNameProvider: PlaceNameProvider by lazy {
    AndroidPlaceNameProvider(this)
  }

  /** Provider for obtaining the current device location. */
  val deviceLocationProvider: DeviceLocationProvider by lazy {
    AndroidLocationProvider(this)
  }

  /** Resolver that combines device location and place name providers. */
  val locationResolver: LocationResolver by lazy {
    CachingLocationResolver(
      delegate = DefaultLocationResolver(
        locationProvider = deviceLocationProvider,
        placeNameProvider = placeNameProvider
      ),
      persistedAppStateRepository = persistedAppStateRepository
    )
  }

  /** WeatherProvider for loading weather in a specific place. */
  val weatherProvider: WeatherProvider by lazy {
    OpenMeteoWeatherProvider()
  }

  override fun onCreate() {
    super.onCreate()
    clearStartupImageCache()
    initOsmdroid()
  }

  /**
   * osmdroid setup:
   *  - userAgentValue is required; otherwise OSM tile servers may block clients
   *    that use the default user agent;
   *  - the tile cache is stored in filesDir instead of cacheDir, because Android
   *    may clear cacheDir when storage is low, causing the app to repeatedly hit
   *    the OSM tile servers.
   */
  private fun initOsmdroid() {
    runCatching {
      val prefs = getSharedPreferences(OSMDROID_PREFS, MODE_PRIVATE)
      val config = Configuration.getInstance()
      config.load(this, prefs)
      config.userAgentValue = packageName
      config.osmdroidBasePath = File(filesDir, "osmdroid").apply { mkdirs() }
      config.osmdroidTileCache = File(config.osmdroidBasePath, "tiles").apply { mkdirs() }
    }.onFailure { error ->
      Log.e(LOG_TAG, "Can't initialize osmdroid: ${error.message}", error)
    }
  }

  /**
   * Clears temporary app image cache on every app process start.
   *
   * This does not touch osmdroid map cache.
   */
  private fun clearStartupImageCache() {
    runCatching {
      ImageCacheCleaner.clearStartupImageCache(this)
    }.onFailure { error ->
      Log.w(LOG_TAG, "Can't clear image cache: ${error.message}", error)
    }
  }

  private companion object {
    const val OSMDROID_PREFS = "osmdroid_prefs"
    const val LOG_TAG = "GeoInspector"
  }
}

/**
 * Returns the current [MyApplication] instance from the Compose context.
 */
@Composable
@ReadOnlyComposable
fun rememberMyApp(): MyApplication {
  return LocalContext.current.applicationContext as MyApplication
}