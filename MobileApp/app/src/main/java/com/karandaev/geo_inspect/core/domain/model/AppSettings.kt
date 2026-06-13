package com.karandaev.geo_inspect.core.domain.model

data class AppSettings(
  val languageCode: String = DEFAULT_LANGUAGE_CODE,
  val themeMode: ThemeMode = ThemeMode.LIGHT,
  val serverBaseUrl: String = DEFAULT_SERVER_BASE_URL
) {
  companion object {
    const val DEFAULT_LANGUAGE_CODE = "en"
    const val DEFAULT_SERVER_BASE_URL = ""
  }
}