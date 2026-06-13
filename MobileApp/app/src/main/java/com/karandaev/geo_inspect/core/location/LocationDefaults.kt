package com.karandaev.geo_inspect.core.location

/**
 * Default location values used as UI fallbacks when no real device location is available.
 */
object LocationDefaults {
  const val STALE_LOCATION_THRESHOLD_MS: Long = 24L * 60L * 60L * 1000L

  const val DEFAULT_PLACE_NAME = "London"
  const val DEFAULT_LATITUDE = 51.5074
  const val DEFAULT_LONGITUDE = -0.1278

  val placeName: String
    get() = DEFAULT_PLACE_NAME

  val location: AppLocation
    get() = AppLocation(
      latitude = DEFAULT_LATITUDE,
      longitude = DEFAULT_LONGITUDE,
      isDefault = true
    )

  val locatedPlace: LocatedPlace
    get() = LocatedPlace(
      location = location,
      placeName = placeName
    )
}