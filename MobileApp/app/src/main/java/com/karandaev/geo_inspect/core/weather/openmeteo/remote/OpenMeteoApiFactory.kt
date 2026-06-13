package com.karandaev.geo_inspect.core.weather.openmeteo.remote

import com.karandaev.geo_inspect.core.network.HttpClientConfig
import com.karandaev.geo_inspect.core.json.MoshiFactory
import com.karandaev.geo_inspect.core.network.OkHttpClientFactory
import com.karandaev.geo_inspect.core.network.RetrofitFactory

/**
 * Creates the default Open-Meteo Retrofit API used by the app.
 */
object OpenMeteoApiFactory {

  /**
   * Creates an [OpenMeteoApi] with default network settings.
   */
  fun create(): OpenMeteoApi {
    val httpClient = OkHttpClientFactory.create(
      config = HttpClientConfig(
        // enableLogging = BuildConfig.DEBUG
      )
    )

    return RetrofitFactory.create(
      baseUrl = OpenMeteoEndpoints.BaseUrl,
      serviceClass = OpenMeteoApi::class.java,
      okHttpClient = httpClient,
      moshi = MoshiFactory.create()
    )
  }
}