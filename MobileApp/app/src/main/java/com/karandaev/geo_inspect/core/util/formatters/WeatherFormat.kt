package com.karandaev.geo_inspect.core.util.formatters

import kotlin.math.roundToInt

fun Double.formatTemperature(): String {
  return formatTemp(this)
}

/**
 * Formats temperature as +12°C / −3°C / 0°C.
 */
private fun formatTemp(
  temperature: Double
): String {
  val rounded = temperature.roundToInt()

  return when {
    rounded > 0 -> "+${rounded}°C"
    rounded < 0 -> "−${-rounded}°C"
    else -> "0°C"
  }
}