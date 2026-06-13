package com.karandaev.geo_inspect.core.network

import java.util.concurrent.TimeUnit

/**
 * Configuration for HTTP clients used by remote data sources.
 *
 * @param connectTimeoutSeconds Maximum time allowed to establish a connection.
 * @param readTimeoutSeconds Maximum time allowed to read data from a server.
 * @param writeTimeoutSeconds Maximum time allowed to write data to a server.
 * @param enableLogging Whether HTTP logging should be enabled.
 */
data class HttpClientConfig(
  val connectTimeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS,
  val readTimeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS,
  val writeTimeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS,
  val enableLogging: Boolean = false
) {
  companion object {
    const val DEFAULT_TIMEOUT_SECONDS = 10L
  }
}

/**
 * Converts seconds to a [TimeUnit.SECONDS] pair used by OkHttp builders.
 */
internal val TimeoutUnit: TimeUnit = TimeUnit.SECONDS