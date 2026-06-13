package com.karandaev.geo_inspect.core.inspection_api.remote

import java.net.URI

private const val DefaultBaseUrlScheme = "http"
private const val DefaultBaseUrlPort = 8443

/**
 * Normalizes user-entered backend base URL.
 *
 * Examples:
 * - 192.168.0.10 -> http://192.168.0.10:8443/
 * - 192.168.0.10:9090 -> http://192.168.0.10:9090/
 * - http://192.168.0.10 -> http://192.168.0.10:8443/
 * - http://192.168.0.10:9090 -> http://192.168.0.10:9090/
 */
internal fun String.normalizeInspectionBaseUrl(): String {
  val value = trim()

  require(value.isNotBlank()) {
    "Base URL must not be blank."
  }

  val withScheme = if (value.hasUrlScheme()) {
    value
  } else {
    "$DefaultBaseUrlScheme://$value"
  }

  val uri = URI(withScheme)

  val scheme = uri.scheme?.lowercase()

  require(scheme == "http" || scheme == "https") {
    "Base URL must use http or https."
  }

  val host = uri.host

  require(!host.isNullOrBlank()) {
    "Base URL host is missing."
  }

  val port = if (uri.port == -1) {
    DefaultBaseUrlPort
  } else {
    uri.port
  }

  val path = uri.path
    ?.takeIf { path -> path.isNotBlank() }
    ?.ensureLeadingSlash()
    ?.ensureTrailingSlash()
    ?: "/"

  return URI(
    scheme,
    null,
    host,
    port,
    path,
    null,
    null
  ).toString()
}

private fun String.hasUrlScheme(): Boolean {
  return contains("://")
}

private fun String.ensureLeadingSlash(): String {
  return if (startsWith("/")) this else "/$this"
}

private fun String.ensureTrailingSlash(): String {
  return if (endsWith("/")) this else "$this/"
}