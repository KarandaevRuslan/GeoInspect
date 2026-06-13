package com.karandaev.geo_inspect.core.weather

/**
 * Domain model representing current weather information.
 *
 * This model is independent from network DTOs, specific weather providers,
 * UI descriptions, icons, and location display names.
 */
data class WeatherInfo(
  val temperature: Double,
  val weatherCode: Int
)