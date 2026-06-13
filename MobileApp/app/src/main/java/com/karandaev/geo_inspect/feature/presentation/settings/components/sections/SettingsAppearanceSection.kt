package com.karandaev.geo_inspect.feature.presentation.settings.components.sections

import androidx.compose.runtime.Composable
import com.karandaev.geo_inspect.core.domain.model.ThemeMode
import com.karandaev.geo_inspect.core.ui.components.toggle_group.ToggleGroup
import com.karandaev.geo_inspect.feature.presentation.settings.components.items.SettingsItem

@Composable
internal fun SettingsAppearanceSection(
  themeMode: ThemeMode,
  onThemeModeSelect: (ThemeMode) -> Unit
) {
  SettingsSection(title = "Appearance") {
    SettingsItem(
      label = "Theme",
      description = "Choose the app color mode"
    ) {
      ToggleGroup(
        options = THEME_MODE_OPTIONS,
        selectedOption = themeMode,
        onSelect = onThemeModeSelect
      )
    }
  }
}

private val THEME_MODE_OPTIONS = linkedMapOf(
  ThemeMode.LIGHT to "Light",
  ThemeMode.DARK to "Dark"
)