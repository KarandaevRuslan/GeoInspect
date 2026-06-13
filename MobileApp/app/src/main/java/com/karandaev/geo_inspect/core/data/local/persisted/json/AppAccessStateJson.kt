package com.karandaev.geo_inspect.core.data.local.persisted.json

import com.squareup.moshi.JsonClass

/**
 * JSON representation of persisted app access state.
 *
 * @param isGuestModeEnabled Whether guest access is enabled.
 */
@JsonClass(generateAdapter = true)
internal data class AppAccessStateJson(
  val isGuestModeEnabled: Boolean = false
)