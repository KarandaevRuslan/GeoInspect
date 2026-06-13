package com.karandaev.geo_inspect.feature.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.app.adaptive_layout.AdaptiveLayoutType
import com.karandaev.geo_inspect.app.adaptive_layout.rememberAdaptiveLayoutType
import com.karandaev.geo_inspect.core.domain.model.ThemeMode
import com.karandaev.geo_inspect.core.presentation.auth.model.UserProfile
import com.karandaev.geo_inspect.core.presentation.settings.ServerAvailabilityState
import com.karandaev.geo_inspect.feature.presentation.settings.contents.SettingsSinglePaneContent
import com.karandaev.geo_inspect.feature.presentation.settings.contents.SettingsTwoPaneContent

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SettingsScreen(
  profile: UserProfile?,
  themeMode: ThemeMode,
  serverBaseUrl: String,
  serverAvailabilityState: ServerAvailabilityState,
  appVersionName: String,
  isExporting: Boolean,
  isImporting: Boolean,
  isResetting: Boolean,
  onProfileClick: () -> Unit,
  onSignInClick: () -> Unit,
  onThemeModeSelect: (ThemeMode) -> Unit,
  onServerBaseUrlClick: () -> Unit,
  onCheckServerClick: () -> Unit,
  onExportClick: () -> Unit,
  onImportClick: () -> Unit,
  onResetClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  BoxWithConstraints(
    modifier = modifier.fillMaxSize()
  ) {
    val layoutType = rememberAdaptiveLayoutType(
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    when (layoutType) {
      AdaptiveLayoutType.SinglePane -> {
        SettingsSinglePaneContent(
          profile = profile,
          themeMode = themeMode,
          serverBaseUrl = serverBaseUrl,
          serverAvailabilityState = serverAvailabilityState,
          appVersionName = appVersionName,
          isExporting = isExporting,
          isImporting = isImporting,
          isResetting = isResetting,
          onProfileClick = onProfileClick,
          onSignInClick = onSignInClick,
          onThemeModeSelect = onThemeModeSelect,
          onServerBaseUrlClick = onServerBaseUrlClick,
          onCheckServerClick = onCheckServerClick,
          onExportClick = onExportClick,
          onImportClick = onImportClick,
          onResetClick = onResetClick,
          modifier = Modifier.fillMaxSize()
        )
      }

      AdaptiveLayoutType.TwoPane -> {
        SettingsTwoPaneContent(
          profile = profile,
          themeMode = themeMode,
          serverBaseUrl = serverBaseUrl,
          serverAvailabilityState = serverAvailabilityState,
          appVersionName = appVersionName,
          isExporting = isExporting,
          isImporting = isImporting,
          isResetting = isResetting,
          onProfileClick = onProfileClick,
          onSignInClick = onSignInClick,
          onThemeModeSelect = onThemeModeSelect,
          onServerBaseUrlClick = onServerBaseUrlClick,
          onCheckServerClick = onCheckServerClick,
          onExportClick = onExportClick,
          onImportClick = onImportClick,
          onResetClick = onResetClick,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}