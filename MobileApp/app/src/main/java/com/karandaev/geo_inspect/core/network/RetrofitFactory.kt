package com.karandaev.geo_inspect.core.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Creates Retrofit service instances for remote APIs.
 */
object RetrofitFactory {

  /**
   * Creates a Retrofit API implementation.
   *
   * @param baseUrl Base URL of the remote API.
   * @param serviceClass Retrofit service interface class.
   * @param okHttpClient HTTP client used by Retrofit.
   * @param moshi Moshi instance used for JSON parsing.
   */
  fun <T> create(
    baseUrl: String,
    serviceClass: Class<T>,
    okHttpClient: OkHttpClient,
    moshi: Moshi
  ): T {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
      .create(serviceClass)
  }
}