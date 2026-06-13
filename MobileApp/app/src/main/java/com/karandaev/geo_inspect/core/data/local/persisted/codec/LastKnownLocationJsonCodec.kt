package com.karandaev.geo_inspect.core.data.local.persisted.codec

import com.karandaev.geo_inspect.core.data.local.persisted.json.LastKnownLocationJson
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.domain.model.persisted.LastKnownLocation
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

/**
 * Encodes and decodes [LastKnownLocation] values stored in persisted app state.
 *
 * @param moshi Moshi instance used for JSON serialization.
 */
internal class LastKnownLocationJsonCodec(
  moshi: Moshi
) {

  private val adapter: JsonAdapter<LastKnownLocationJson> =
    moshi.adapter(LastKnownLocationJson::class.java)

  /**
   * Converts a domain last known location into a JSON string.
   */
  fun encode(value: LastKnownLocation): String {
    return adapter.toJson(
      LastKnownLocationJson(
        latitude = value.latitude,
        longitude = value.longitude,
        placeName = value.placeName,
        updatedAt = value.updatedAt
      )
    )
  }

  /**
   * Converts a JSON string into a domain last known location.
   *
   * Returns `null` when the stored value is invalid or cannot be decoded.
   */
  fun decodeOrNull(json: String): LastKnownLocation? {
    return runCatching {
      val decoded = adapter.fromJson(json)
        ?: return null

      LastKnownLocation(
        point = MapPoint(
          latitude = decoded.latitude,
          longitude = decoded.longitude
        ),
        placeName = decoded.placeName,
        updatedAt = decoded.updatedAt
      )
    }.getOrNull()
  }
}