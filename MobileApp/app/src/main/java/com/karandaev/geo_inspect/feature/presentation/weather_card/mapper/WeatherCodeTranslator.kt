package com.karandaev.geo_inspect.feature.presentation.weather_card.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WaterDrop
import com.karandaev.geo_inspect.core.ui.components.card.AppCardMedia

/**
 * UI interpretation of a weather code.
 */
data class WeatherCodeUi(
  val description: String,
  val icon: AppCardMedia
)

/**
 * Maps weather provider codes to UI labels and icons.
 *
 * Open-Meteo uses WMO weather interpretation codes.
 */
fun Int.toWeatherCodeUi(): WeatherCodeUi {
  return when (this) {
    0 -> WeatherCodeUi(
      description = "Clear sky",
      icon = AppCardMedia.Icon(Icons.Default.Cloud)
    )

    1, 2, 3 -> WeatherCodeUi(
      description = "Partly cloudy",
      icon = AppCardMedia.Icon(Icons.Default.Cloud)
    )

    45, 48 -> WeatherCodeUi(
      description = "Fog",
      icon = AppCardMedia.Icon(Icons.Default.Cloud)
    )

    51, 53, 55, 56, 57 -> WeatherCodeUi(
      description = "Drizzle",
      icon = AppCardMedia.Icon(Icons.Default.WaterDrop)
    )

    61, 63, 65, 66, 67, 80, 81, 82 -> WeatherCodeUi(
      description = "Rain",
      icon = AppCardMedia.Icon(Icons.Default.Umbrella)
    )

    71, 73, 75, 77, 85, 86 -> WeatherCodeUi(
      description = "Snow",
      icon = AppCardMedia.Icon(Icons.Default.Grain)
    )

    95, 96, 99 -> WeatherCodeUi(
      description = "Thunderstorm",
      icon = AppCardMedia.Icon(Icons.Default.Thunderstorm)
    )

    else -> WeatherCodeUi(
      description = "Unknown weather",
      icon = AppCardMedia.Icon(Icons.Default.Cloud)
    )
  }
}