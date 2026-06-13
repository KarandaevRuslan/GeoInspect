package com.karandaev.geo_inspect.core.location.place

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

private const val MaxAddressResults = 1

/**
 * Resolves human-readable place names using Android's [Geocoder].
 *
 * This implementation hides API-level differences between the deprecated blocking geocoder API
 * and the asynchronous API introduced on Android 13.
 *
 * The returned place name is selected from the most specific available address field in this order:
 * locality, sub-administrative area, administrative area, country name.
 *
 * @param context Android context used to create the [Geocoder].
 */
class AndroidPlaceNameProvider(
  context: Context
) : PlaceNameProvider {

  private val applicationContext = context.applicationContext

  /**
   * Returns a human-readable place name for the provided coordinates.
   *
   * Returns an empty string if Android Geocoder cannot resolve an address.
   */
  override suspend fun getPlaceName(
    latitude: Double,
    longitude: Double
  ): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      getPlaceNameAsync(latitude, longitude)
    } else {
      getPlaceNameBlocking(latitude, longitude)
    }
  }

  @Suppress("DEPRECATION")
  private suspend fun getPlaceNameBlocking(
    latitude: Double,
    longitude: Double
  ): String = withContext(Dispatchers.IO) {
    runCatching {
      createGeocoder()
        .getFromLocation(latitude, longitude, MaxAddressResults)
        ?.firstOrNull()
        .toPlaceName()
    }.getOrDefault("")
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  private suspend fun getPlaceNameAsync(
    latitude: Double,
    longitude: Double
  ): String = suspendCancellableCoroutine { continuation ->
    createGeocoder().getFromLocation(
      latitude,
      longitude,
      MaxAddressResults
    ) { addresses ->
      if (!continuation.isActive) return@getFromLocation

      val placeName = addresses
        .firstOrNull()
        .toPlaceName()

      continuation.resume(placeName)
    }
  }

  private fun createGeocoder(): Geocoder {
    return Geocoder(applicationContext, Locale.getDefault())
  }
}