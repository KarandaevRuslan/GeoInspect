package com.karandaev.geo_inspect.core.inspection_api.remote

import com.karandaev.geo_inspect.core.json.MoshiFactory
import com.karandaev.geo_inspect.core.network.HttpClientConfig
import com.karandaev.geo_inspect.core.network.OkHttpClientFactory
import com.karandaev.geo_inspect.core.network.RetrofitFactory

/**
 * Creates geo inspection Retrofit API instances.
 */
object InspectionApiFactory {

  /**
   * Creates a [InspectionApi] for the provided backend URL.
   *
   * @param baseUrl Base URL of the geo inspection backend.
   * For example: `http://192.168.0.10:8443/`.
   * Retrofit requires the URL to end with `/`.
   * @param config HTTP client configuration.
   */
  fun create(
    baseUrl: String,
    config: HttpClientConfig = HttpClientConfig()
  ): InspectionApi {
    val httpClient = OkHttpClientFactory.create(config = config)

    return RetrofitFactory.create(
      baseUrl = baseUrl.normalizeInspectionBaseUrl(),
      serviceClass = InspectionApi::class.java,
      okHttpClient = httpClient,
      moshi = MoshiFactory.create()
    )
  }
}