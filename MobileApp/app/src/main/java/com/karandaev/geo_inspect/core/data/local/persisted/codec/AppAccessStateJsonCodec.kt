package com.karandaev.geo_inspect.core.data.local.persisted.codec

import com.karandaev.geo_inspect.core.data.local.persisted.json.AppAccessStateJson
import com.karandaev.geo_inspect.core.domain.model.persisted.AppAccessState
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

/**
 * Encodes and decodes [AppAccessState] values stored in persisted app state.
 *
 * @param moshi Moshi instance used for JSON serialization.
 */
internal class AppAccessStateJsonCodec(
  moshi: Moshi
) {

  private val adapter: JsonAdapter<AppAccessStateJson> =
    moshi.adapter(AppAccessStateJson::class.java)

  /**
   * Converts a domain app access state into a JSON string.
   */
  fun encode(value: AppAccessState): String {
    return adapter.toJson(
      AppAccessStateJson(
        isGuestModeEnabled = value.isGuestModeEnabled
      )
    )
  }

  /**
   * Converts a JSON string into a domain app access state.
   *
   * Returns `null` when the stored value is invalid or cannot be decoded.
   */
  fun decodeOrNull(json: String): AppAccessState? {
    return runCatching {
      val decoded = adapter.fromJson(json)
        ?: return null

      AppAccessState(
        isGuestModeEnabled = decoded.isGuestModeEnabled
      )
    }.getOrNull()
  }
}