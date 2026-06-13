package com.karandaev.geo_inspect.core.domain.model

/**
 * Supported application theme modes.
 */
enum class ThemeMode {
  LIGHT,
  DARK;

  companion object {

    /**
     * Parses a stored theme mode value.
     *
     * Unknown values fall back to [LIGHT].
     */
    fun from(value: String?): ThemeMode {
      return entries.firstOrNull { mode -> mode.name == value } ?: LIGHT
    }
  }
}