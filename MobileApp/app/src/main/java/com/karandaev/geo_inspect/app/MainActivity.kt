package com.karandaev.geo_inspect.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karandaev.geo_inspect.core.presentation.persisted.PersistedAppStateViewModel
import com.karandaev.geo_inspect.core.presentation.settings.SettingsViewModel
import com.karandaev.geo_inspect.core.ui.theme.MyTheme

/**
 * Main activity that hosts the MapNotes Compose application.
 *
 * The activity is responsible only for setting up edge-to-edge mode, creating the root
 * application dependencies, collecting global settings, and applying the app theme.
 */
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    setContent {
      val app = rememberMyApp()

      val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.factory(
          settingsRepository = app.settingsRepository,
          inspectionReportRepository = app.inspectionReportRepository,
          inspectionProvider = app.inspectionProvider
        )
      )

      val persistedAppStateViewModel: PersistedAppStateViewModel = viewModel(
        factory = PersistedAppStateViewModel.factory(
          persistedAppStateRepository = app.persistedAppStateRepository
        )
      )

      val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

      MyTheme(
        themeMode = settings.themeMode
      ) {
        MyApp(
          app = app,
          settingsViewModel = settingsViewModel,
          persistedAppStateViewModel = persistedAppStateViewModel
        )
      }
    }
  }
}