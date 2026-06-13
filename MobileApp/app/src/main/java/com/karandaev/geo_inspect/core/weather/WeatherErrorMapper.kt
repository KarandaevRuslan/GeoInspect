package com.karandaev.geo_inspect.core.weather

import com.karandaev.geo_inspect.core.weather.openmeteo.OpenMeteoWeatherParseException
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.io.IOException

/**
 * Maps low-level weather loading errors to user-facing domain errors.
 */
internal fun Throwable.toWeatherError(): Throwable {
  return when (this) {
    is HttpException -> IllegalStateException(
      "Weather server error: ${code()}",
      this
    )

    is IOException -> IllegalStateException(
      "No connection to the weather service",
      this
    )

    is JsonDataException,
    is OpenMeteoWeatherParseException -> IllegalStateException(
      "Invalid response from the weather service",
      this
    )

    else -> IllegalStateException(
      "Weather loading error: ${message ?: this::class.simpleName}",
      this
    )
  }
}