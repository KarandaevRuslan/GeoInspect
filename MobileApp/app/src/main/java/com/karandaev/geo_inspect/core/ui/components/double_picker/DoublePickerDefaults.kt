package com.karandaev.geo_inspect.core.ui.components.double_picker

import java.util.Locale

/**
 * Default values and formatting helpers for [DoublePicker].
 */
object DoublePickerDefaults {
  const val Step: Double = 0.0001
  const val Min: Double = -180.0
  const val Max: Double = 180.0

  private const val DefaultFormat = "%.5f"

  /**
   * Formats a double value using a stable US locale decimal separator.
   */
  fun formatValue(value: Double): String {
    return String.format(Locale.US, DefaultFormat, value)
  }

  /**
   * Parses user input into a double value.
   *
   * Both comma and dot decimal separators are supported.
   */
  fun parseValue(input: String): Double? {
    return input
      .trim()
      .replace(',', '.')
      .toDoubleOrNull()
  }
}