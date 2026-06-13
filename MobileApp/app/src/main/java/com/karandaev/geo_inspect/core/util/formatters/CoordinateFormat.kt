package com.karandaev.geo_inspect.core.util.formatters

import java.util.Locale

private const val CoordinatePrecision = "%.4f"

/**
 * Formats a coordinate value using a stable US locale decimal separator.
 *
 * @param value Coordinate value to format.
 * @return Coordinate string with fixed precision.
 */
fun formatCoordinate(value: Double): String {
  return String.format(Locale.US, CoordinatePrecision, value)
}

/**
 * Formats a coordinate pair using a stable US locale decimal separator.
 *
 * @param latitude Latitude value to format.
 * @param longitude Longitude value to format.
 * @return Coordinate pair string with fixed precision.
 */
fun formatCoordinates(
  latitude: Double,
  longitude: Double
): String {
  return String.format(
    Locale.US,
    "$CoordinatePrecision, $CoordinatePrecision",
    latitude,
    longitude
  )
}
