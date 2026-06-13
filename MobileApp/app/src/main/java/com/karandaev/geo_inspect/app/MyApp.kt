package com.karandaev.geo_inspect.app

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.karandaev.geo_inspect.app.adaptive_layout.rememberAdaptiveLayoutType
import com.karandaev.geo_inspect.app.navigation.AppNavHost
import com.karandaev.geo_inspect.app.navigation.Routes
import com.karandaev.geo_inspect.app.navigation.rememberAppDestination
import com.karandaev.geo_inspect.app.scaffold.GeoInspectScaffold
import com.karandaev.geo_inspect.app.splash.AppSplashScreen
import com.karandaev.geo_inspect.app.splash.AppSplashViewModel
import com.karandaev.geo_inspect.core.presentation.auth.AuthViewModel
import com.karandaev.geo_inspect.core.presentation.auth.model.canAccessApp
import com.karandaev.geo_inspect.core.presentation.auth.factory
import com.karandaev.geo_inspect.core.presentation.detection.DetectViewModel
import com.karandaev.geo_inspect.core.presentation.message.UiMessageDisplayMode
import com.karandaev.geo_inspect.core.presentation.message.shouldShowAsToast
import com.karandaev.geo_inspect.core.presentation.reports.InspectionReportsViewModel
import com.karandaev.geo_inspect.core.presentation.persisted.PersistedAppStateViewModel
import com.karandaev.geo_inspect.core.presentation.settings.SettingsViewModel
import com.karandaev.geo_inspect.core.util.other.toast
import com.karandaev.geo_inspect.feature.presentation.auth.navigation.isAuth

/**
 * Root composable of the application.
 *
 * This function wires navigation, screen-level ViewModels, adaptive layout,
 * and the application scaffold together.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MyApp(
  app: MyApplication,
  settingsViewModel: SettingsViewModel,
  persistedAppStateViewModel: PersistedAppStateViewModel
) {
  val inspectionReportsViewModel: InspectionReportsViewModel = viewModel(
    factory = InspectionReportsViewModel.factory(app.inspectionReportRepository)
  )

  val authViewModel: AuthViewModel = viewModel(
    factory = AuthViewModel.factory(
      auth = app.firebaseAuth,
      firebaseGoogleAuthProvider = app.firebaseGoogleAuthProvider,
      persistedAppStateRepository = app.persistedAppStateRepository,
      inspectionProvider = app.inspectionProvider,
      imageSourceFileProvider = app.imageSourceFileProvider,
      imageCropper = app.imageCropper
    )
  )

  val detectViewModel: DetectViewModel = viewModel(
    factory = DetectViewModel.factory(
      inspectionProvider = app.inspectionProvider,
      imageSourceFileProvider = app.imageSourceFileProvider,
      imageCropper = app.imageCropper
    )
  )

  val splashViewModel: AppSplashViewModel = viewModel()

  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()

  val inspectionReports by inspectionReportsViewModel.inspectionReports.collectAsStateWithLifecycle()
  val authState by authViewModel.state.collectAsStateWithLifecycle()
  val isSplashVisible by splashViewModel.isSplashVisible.collectAsStateWithLifecycle()

  val context = LocalContext.current
  val authMessage = authState.message

  val currentRoute = navBackStackEntry?.destination?.route
  val isNavigationReady = currentRoute != null
  val isAuthRoute = currentRoute.isAuth()
  val canAccessApp = authState.canAccessApp

  val destination = rememberAppDestination(
    navBackStackEntry = navBackStackEntry,
    inspectionReports = inspectionReports
  )

  LaunchedEffect(authMessage?.refreshId) {
    val message = authMessage ?: return@LaunchedEffect

    if (!message.shouldShowAsToast) {
      return@LaunchedEffect
    }

    context.toast(message.text)

    if (message.displayMode == UiMessageDisplayMode.Toast) {
      authViewModel.clearMessage()
    }
  }

  LaunchedEffect(
    canAccessApp,
    currentRoute
  ) {
    if (!isNavigationReady) {
      return@LaunchedEffect
    }

    if (!canAccessApp && !isAuthRoute) {
      navController.navigate(Routes.AUTH_LOGIN) {
        popUpTo(navController.graph.id) {
          inclusive = true
        }
        launchSingleTop = true
      }
      return@LaunchedEffect
    }

    if (canAccessApp && isAuthRoute) {
      navController.navigate(Routes.HOME) {
        popUpTo(navController.graph.id) {
          inclusive = true
        }
        launchSingleTop = true
      }
    }
  }

  BoxWithConstraints {
    val layoutType = rememberAdaptiveLayoutType(
      maxWidth = maxWidth,
      maxHeight = maxHeight
    )

    Box {
      GeoInspectScaffold(
        destination = destination,
        layoutType = layoutType,
        navController = navController
      ) { contentModifier ->
        AppNavHost(
          navController = navController,
          inspectionReportsViewModel = inspectionReportsViewModel,
          settingsViewModel = settingsViewModel,
          persistedAppStateViewModel = persistedAppStateViewModel,
          authViewModel = authViewModel,
          detectViewModel = detectViewModel,
          inspectionReports = inspectionReports,
          modifier = contentModifier
        )
      }

      if (isSplashVisible) {
        AppSplashScreen()
      }
    }
  }
}