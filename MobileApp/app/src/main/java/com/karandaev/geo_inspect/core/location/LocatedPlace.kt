package com.karandaev.geo_inspect.core.location

/**
 * Combined app-level location model with a human-readable place name.
 *
 * This model is useful for UI and feature layers that need both coordinates and a display label.
 *
 * @param location App-level location coordinates and metadata.
 * @param placeName Human-readable place name resolved from the location.
 */
data class LocatedPlace(
  val location: AppLocation,
  val placeName: String
) {
  val latitude: Double
    get() = location.latitude

  val longitude: Double
    get() = location.longitude

  val isDefault: Boolean
    get() = location.isDefault
}