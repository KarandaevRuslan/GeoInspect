package com.karandaev.geo_inspect.core.location.provider

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import com.karandaev.geo_inspect.core.location.AppLocation
import com.karandaev.geo_inspect.core.location.LocationDefaults
import com.karandaev.geo_inspect.core.location.permission_checker.LocationPermissionChecker
import com.karandaev.geo_inspect.core.location.provider.LocationScorer.isFreshEnough
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

private val AllLocationProviders = listOf(
  LocationManager.GPS_PROVIDER,
  LocationManager.NETWORK_PROVIDER,
  LocationManager.PASSIVE_PROVIDER
)

private val FreshLocationProviders = listOf(
  LocationManager.GPS_PROVIDER,
  LocationManager.NETWORK_PROVIDER
)

/**
 * Provides device location using Android's system [LocationManager].
 *
 * This provider does not require Google Play Services. It combines a small in-memory cache,
 * fresh single-location requests, and system last-known locations to return the best available
 * app-level location with minimal repeated location polling.
 *
 * @param context Android context used to access location system services and permissions.
 * @param config Runtime configuration for timeouts, cache age, and refresh frequency.
 */
class AndroidLocationProvider(
  context: Context,
  private val config: LocationProviderConfig = LocationProviderConfig()
) : DeviceLocationProvider {

  private val applicationContext = context.applicationContext

  private var cachedBestLocation: Location? = null
  private var lastFreshLocationRequestAtElapsedRealtimeMillis: Long = 0L
  private var locationLookupCount: Int = 0

  /**
   * Returns the best currently available real device location.
   *
   * The lookup order is:
   * 1. Recent in-memory cached location.
   * 2. Fresh current location if refresh policy allows it.
   * 3. Best last-known system location.
   *
   * Returns null when location permission is missing or no provider can produce a usable location.
   */
  override suspend fun getLocation(): AppLocation? {
    return getBestAvailableAndroidLocation()
      ?.toAppLocation()
  }

  /**
   * Returns the best currently available Android framework location.
   *
   * This method is kept internal to this provider because the rest of the app should use
   * [AppLocation] through [getLocation].
   */
  private suspend fun getBestAvailableAndroidLocation(): Location? {
    if (!LocationPermissionChecker.hasLocationPermission(applicationContext)) return null

    val nowElapsedRealtimeMillis = SystemClock.elapsedRealtime()

    getFreshCachedLocation(
      nowElapsedRealtimeMillis = nowElapsedRealtimeMillis,
      maxAgeMillis = config.cacheMaxAgeMillis
    )?.let { cachedLocation ->
      return cachedLocation
    }

    val freshLocation = requestFreshLocationIfNeeded(
      nowElapsedRealtimeMillis = nowElapsedRealtimeMillis
    )

    if (freshLocation != null) {
      markFreshLocationRequestSucceeded(
        nowElapsedRealtimeMillis = nowElapsedRealtimeMillis,
        location = freshLocation
      )

      return freshLocation
    }

    val lastKnownLocation = getLastKnownAndroidLocation()

    val bestLocation = LocationScorer.chooseBestLocation(
      locations = listOfNotNull(cachedBestLocation, lastKnownLocation),
      nowElapsedRealtimeMillis = nowElapsedRealtimeMillis
    )

    if (bestLocation != null) {
      updateCache(bestLocation)
    }

    return bestLocation
  }

  /**
   * Returns the best last-known Android framework location.
   *
   * Stale locations older than [maxAgeMillis] are ignored.
   */
  private fun getLastKnownAndroidLocation(
    maxAgeMillis: Long = LocationDefaults.STALE_LOCATION_THRESHOLD_MS
  ): Location? {
    if (!LocationPermissionChecker.hasLocationPermission(applicationContext)) return null

    val manager = applicationContext.locationManagerOrNull() ?: return null
    val nowElapsedRealtimeMillis = SystemClock.elapsedRealtime()

    val candidates = AllLocationProviders
      .filter { provider -> manager.isProviderEnabledSafe(provider) }
      .mapNotNull { provider -> manager.getLastKnownLocationSafe(provider) }
      .filter { location ->
        location.isFreshEnough(
          nowElapsedRealtimeMillis = nowElapsedRealtimeMillis,
          maxAgeMillis = maxAgeMillis
        )
      }

    return LocationScorer.chooseBestLocation(
      locations = candidates,
      nowElapsedRealtimeMillis = nowElapsedRealtimeMillis
    )
  }

  /**
   * Requests a fresh single Android framework location update.
   *
   * GPS is tried first because it is usually more accurate. Network is tried next as a fallback
   * because it is often faster indoors.
   */
  private suspend fun getCurrentAndroidLocation(): Location? {
    if (!LocationPermissionChecker.hasLocationPermission(applicationContext)) return null

    val manager = applicationContext.locationManagerOrNull() ?: return null

    val enabledProviders = FreshLocationProviders
      .filter { provider -> manager.isProviderEnabledSafe(provider) }

    for (provider in enabledProviders) {
      val location = requestSingleLocation(
        manager = manager,
        provider = provider,
        timeoutMillis = config.currentLocationTimeoutMillis
      )

      if (location != null) {
        return location
      }
    }

    return null
  }

  @Synchronized
  private fun getFreshCachedLocation(
    nowElapsedRealtimeMillis: Long,
    maxAgeMillis: Long
  ): Location? {
    return cachedBestLocation
      ?.takeIf { location ->
        location.isFreshEnough(
          nowElapsedRealtimeMillis = nowElapsedRealtimeMillis,
          maxAgeMillis = maxAgeMillis
        )
      }
  }

  /**
   * Requests a fresh location only when refresh policy allows it.
   *
   * Failed fresh requests do not update refresh throttling timestamps. This allows
   * the app to retry immediately after the user enables location services.
   */
  private suspend fun requestFreshLocationIfNeeded(
    nowElapsedRealtimeMillis: Long
  ): Location? {
    if (!shouldRequestFreshLocation(nowElapsedRealtimeMillis)) {
      return null
    }

    return getCurrentAndroidLocation()
  }

  private fun isFreshLocationRefreshIntervalElapsed(
    nowElapsedRealtimeMillis: Long
  ): Boolean {
    return nowElapsedRealtimeMillis - lastFreshLocationRequestAtElapsedRealtimeMillis >=
      config.freshLocationRefreshIntervalMillis
  }

  private fun isFreshLocationRefreshNthCall(): Boolean {
    return config.freshLocationRefreshEveryNthCall > 0 &&
      locationLookupCount % config.freshLocationRefreshEveryNthCall == 0
  }

  @Synchronized
  private fun shouldRequestFreshLocation(
    nowElapsedRealtimeMillis: Long
  ): Boolean {
    locationLookupCount += 1

    return isFreshLocationRefreshIntervalElapsed(nowElapsedRealtimeMillis) ||
      isFreshLocationRefreshNthCall()
  }

  @Synchronized
  private fun updateCache(location: Location) {
    cachedBestLocation = location
  }

  @Synchronized
  private fun markFreshLocationRequestSucceeded(
    nowElapsedRealtimeMillis: Long,
    location: Location
  ) {
    lastFreshLocationRequestAtElapsedRealtimeMillis = nowElapsedRealtimeMillis
    updateCache(location)
  }

  private suspend fun requestSingleLocation(
    manager: LocationManager,
    provider: String,
    timeoutMillis: Long
  ): Location? {
    return withTimeoutOrNull(timeoutMillis) {
      suspendCancellableCoroutine { continuation ->
        val listener = object : LocationListener {
          override fun onLocationChanged(location: Location) {
            manager.removeUpdates(this)

            if (continuation.isActive) {
              continuation.resume(location)
            }
          }

          override fun onProviderDisabled(provider: String) {
            manager.removeUpdates(this)

            if (continuation.isActive) {
              continuation.resume(null)
            }
          }

          @Deprecated("Deprecated in Android API")
          override fun onStatusChanged(
            provider: String?,
            status: Int,
            extras: Bundle?
          ) = Unit
        }

        continuation.invokeOnCancellation {
          manager.removeUpdates(listener)
        }

        runCatching {
          @Suppress("MissingPermission")
          manager.requestLocationUpdates(
            provider,
            0L,
            0f,
            listener,
            Looper.getMainLooper()
          )
        }.onFailure {
          manager.removeUpdates(listener)

          if (continuation.isActive) {
            continuation.resume(null)
          }
        }
      }
    }
  }
}