package com.karandaev.geo_inspect.core.json

import com.squareup.moshi.Moshi

/**
 * Creates Moshi instances.
 *
 * Moshi codegen is expected. DTO classes should use
 * `@JsonClass(generateAdapter = true)`.
 */
object MoshiFactory {

  /**
   * Creates a default [com.squareup.moshi.Moshi] instance.
   */
  fun create(): Moshi {
    return Moshi.Builder().build()
  }
}