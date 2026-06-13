package com.karandaev.geo_inspect.feature.presentation.settings.components.sections

import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.feature.presentation.settings.components.about.SettingsAboutContent

@Composable
internal fun SettingsAboutSection(
  appVersionName: String
) {
  SettingsSection(title = "About") {
    SettingsAboutContent(
      appVersionName = appVersionName
    )
  }
}