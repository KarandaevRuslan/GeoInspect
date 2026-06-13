package com.karandaev.geo_inspect.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Creates configured [OkHttpClient] instances for remote data sources.
 */
object OkHttpClientFactory {

  /**
   * Creates an [OkHttpClient] using the provided [config].
   */
  fun create(config: HttpClientConfig): OkHttpClient {
    return OkHttpClient.Builder()
      .connectTimeout(config.connectTimeoutSeconds, TimeoutUnit)
      .readTimeout(config.readTimeoutSeconds, TimeoutUnit)
      .writeTimeout(config.writeTimeoutSeconds, TimeoutUnit)
      .apply {
        if (config.enableLogging) {
          addInterceptor(createLoggingInterceptor())
        }
      }
      .build()
  }

  private fun createLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BASIC
    }
  }
}