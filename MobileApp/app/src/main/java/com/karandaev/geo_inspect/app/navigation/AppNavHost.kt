package com.karandaev.geo_inspect.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.presentation.auth.AuthViewModel
import com.karandaev.geo_inspect.core.presentation.detection.DetectViewModel
import com.karandaev.geo_inspect.core.presentation.reports.InspectionReportsViewModel
import com.karandaev.geo_inspect.core.presentation.persisted.PersistedAppStateViewModel
import com.karandaev.geo_inspect.core.presentation.settings.SettingsViewModel
import com.karandaev.geo_inspect.feature.presentation.auth.login.LoginRoute
import com.karandaev.geo_inspect.feature.presentation.auth.register.RegisterRoute
import com.karandaev.geo_inspect.feature.presentation.auth.reset.PasswordResetRoute
import com.karandaev.geo_inspect.feature.presentation.create.CreateInspectionReportRoute
import com.karandaev.geo_inspect.feature.presentation.detection.DetectRoute
import com.karandaev.geo_inspect.feature.presentation.home.HomeRoute
import com.karandaev.geo_inspect.feature.presentation.map.MapRoute
import com.karandaev.geo_inspect.feature.presentation.profile.ProfileRoute
import com.karandaev.geo_inspect.feature.presentation.settings.SettingsRoute
import com.karandaev.geo_inspect.feature.presentation.view.ViewInspectionReportRoute

/**
 * Main application NavHost.
 *
 * Owns screen route registration and connects navigation events from screens
 * to the root [NavHostController].
 */
@Composable
fun AppNavHost(
  navController: NavHostController,
  inspectionReportsViewModel: InspectionReportsViewModel,
  settingsViewModel: SettingsViewModel,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  authViewModel: AuthViewModel,
  detectViewModel: DetectViewModel,
  inspectionReports: List<InspectionReport>,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    NavHost(
      navController = navController,
      startDestination = Routes.AUTH_LOGIN
    ) {
      authRoutes(
        authViewModel = authViewModel,
        onLoginClick = {
          navController.navigateToAuthRoute(
            route = Routes.AUTH_LOGIN,
            authViewModel = authViewModel
          )
        },
        onRegisterSuccess = {
          navController.navigateToAuthRoute(
            route = Routes.AUTH_LOGIN,
            authViewModel = authViewModel,
            clearMessage = false
          )
        },
        onPasswordResetSuccess = {
          navController.navigateToAuthRoute(
            route = Routes.AUTH_LOGIN,
            authViewModel = authViewModel,
            clearMessage = false
          )
        },
        onRegisterClick = {
          navController.navigateToAuthRoute(
            route = Routes.AUTH_REGISTER,
            authViewModel = authViewModel
          )
        },
        onPasswordResetClick = {
          navController.navigateToAuthRoute(
            route = Routes.AUTH_PASSWORD_RESET,
            authViewModel = authViewModel
          )
        }
      )

      homeRoute(
        inspectionReports = inspectionReports,
        inspectionReportsViewModel = inspectionReportsViewModel,
        onInspectionReportClick = { inspectionReportId ->
          navController.navigate(Routes.viewNote(inspectionReportId))
        },
        onInspectionReportEdit = { inspectionReportId ->
          navController.navigate(Routes.editNote(inspectionReportId))
        }
      )

      mapRoute(
        inspectionReports = inspectionReports,
        persistedAppStateViewModel = persistedAppStateViewModel,
        onMarkerClick = { inspectionReportId ->
          navController.navigate(Routes.viewNote(inspectionReportId))
        },
        onLongPressCreateInspectionReport = { latitude, longitude ->
          navController.navigate(Routes.create(latitude, longitude))
        }
      )

      detectRoute(
        detectViewModel = detectViewModel,
        onBackClick = navController::popBackStack,
        onDetectionComplete = {
          navController.popBackStack()
        }
      )

      profileRoute(
        authViewModel = authViewModel,
        onBackClick = navController::popBackStack
      )

      settingsRoute(
        settingsViewModel = settingsViewModel,
        authViewModel = authViewModel,
        inspectionReports = inspectionReports,
        onProfileClick = {
          navController.navigate(Routes.PROFILE)
        },
        onSignInClick = {
          authViewModel.clearGuestModeSilently()
        }
      )

      createInspectionReportRoute(
        inspectionReportsViewModel = inspectionReportsViewModel,
        persistedAppStateViewModel = persistedAppStateViewModel,
        detectViewModel = detectViewModel,
        onDetectClick = {
          detectViewModel.resetForNewDetection()
          navController.navigate(Routes.DETECT)
        },
        onBackClick = navController::popBackStack
      )

      editInspectionReportRoute(
        inspectionReportsViewModel = inspectionReportsViewModel,
        persistedAppStateViewModel = persistedAppStateViewModel,
        detectViewModel = detectViewModel,
        onDetectClick = {
          detectViewModel.resetForNewDetection()
          navController.navigate(Routes.DETECT)
        },
        onBackClick = navController::popBackStack
      )

      viewInspectionReportRoute(
        inspectionReportsViewModel = inspectionReportsViewModel,
        persistedAppStateViewModel = persistedAppStateViewModel,
        onBackClick = navController::popBackStack,
        onEditClick = { inspectionReportId ->
          navController.navigate(Routes.editNote(inspectionReportId))
        }
      )
    }
  }
}

private fun NavGraphBuilder.authRoutes(
  authViewModel: AuthViewModel,
  onLoginClick: () -> Unit,
  onRegisterSuccess: () -> Unit,
  onPasswordResetSuccess: () -> Unit,
  onRegisterClick: () -> Unit,
  onPasswordResetClick: () -> Unit
) {
  composable(Routes.AUTH_LOGIN) {
    LoginRoute(
      authViewModel = authViewModel,
      onRegisterClick = onRegisterClick,
      onPasswordResetClick = onPasswordResetClick
    )
  }

  composable(Routes.AUTH_REGISTER) {
    RegisterRoute(
      authViewModel = authViewModel,
      onLoginClick = onLoginClick,
      onRegisterSuccess = onRegisterSuccess
    )
  }

  composable(Routes.AUTH_PASSWORD_RESET) {
    PasswordResetRoute(
      authViewModel = authViewModel,
      onLoginClick = onLoginClick,
      onPasswordResetSuccess = onPasswordResetSuccess
    )
  }
}

private fun NavGraphBuilder.detectRoute(
  detectViewModel: DetectViewModel,
  onBackClick: () -> Unit,
  onDetectionComplete: () -> Unit
) {
  composable(Routes.DETECT) {
    DetectRoute(
      detectViewModel = detectViewModel,
      onBackClick = onBackClick,
      onDetectionComplete = onDetectionComplete
    )
  }
}

private fun NavGraphBuilder.profileRoute(
  authViewModel: AuthViewModel,
  onBackClick: () -> Unit
) {
  composable(Routes.PROFILE) {
    ProfileRoute(
      authViewModel = authViewModel,
      onBackClick = onBackClick
    )
  }
}

private fun NavGraphBuilder.homeRoute(
  inspectionReports: List<InspectionReport>,
  inspectionReportsViewModel: InspectionReportsViewModel,
  onInspectionReportClick: (Long) -> Unit,
  onInspectionReportEdit: (Long) -> Unit
) {
  composable(Routes.HOME) {
    HomeRoute(
      inspectionReports = inspectionReports,
      onInspectionReportClick = onInspectionReportClick,
      onInspectionReportEdit = onInspectionReportEdit,
      onInspectionReportDelete = { inspectionReportId ->
        inspectionReportsViewModel.deleteInspectionReport(inspectionReportId)
      }
    )
  }
}

private fun NavGraphBuilder.mapRoute(
  inspectionReports: List<InspectionReport>,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  onMarkerClick: (Long) -> Unit,
  onLongPressCreateInspectionReport: (Double, Double) -> Unit
) {
  composable(Routes.MAP) {
    MapRoute(
      inspectionReports = inspectionReports,
      persistedAppStateViewModel = persistedAppStateViewModel,
      onMarkerClick = onMarkerClick,
      onLongPressCreateNote = onLongPressCreateInspectionReport
    )
  }
}

private fun NavGraphBuilder.settingsRoute(
  settingsViewModel: SettingsViewModel,
  authViewModel: AuthViewModel,
  inspectionReports: List<InspectionReport>,
  onProfileClick: () -> Unit,
  onSignInClick: () -> Unit
) {
  composable(Routes.SETTINGS) {
    SettingsRoute(
      settingsViewModel = settingsViewModel,
      authViewModel = authViewModel,
      inspectionReports = inspectionReports,
      onProfileClick = onProfileClick,
      onSignInClick = onSignInClick
    )
  }
}

private fun NavGraphBuilder.createInspectionReportRoute(
  inspectionReportsViewModel: InspectionReportsViewModel,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  detectViewModel: DetectViewModel,
  onDetectClick: () -> Unit,
  onBackClick: () -> Unit
) {
  composable(
    route = Routes.CREATE_PATTERN,
    arguments = listOf(
      navArgument(Routes.ARG_LAT) {
        type = NavType.StringType
        nullable = true
        defaultValue = null
      },
      navArgument(Routes.ARG_LON) {
        type = NavType.StringType
        nullable = true
        defaultValue = null
      }
    )
  ) { backStackEntry ->
    CreateInspectionReportRoute(
      inspectionReportsViewModel = inspectionReportsViewModel,
      persistedAppStateViewModel = persistedAppStateViewModel,
      detectViewModel = detectViewModel,
      prefilledLatitude = backStackEntry.getOptionalDoubleArgument(Routes.ARG_LAT),
      prefilledLongitude = backStackEntry.getOptionalDoubleArgument(Routes.ARG_LON),
      onDetectClick = onDetectClick,
      onBackClick = onBackClick
    )
  }
}

private fun NavGraphBuilder.editInspectionReportRoute(
  inspectionReportsViewModel: InspectionReportsViewModel,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  detectViewModel: DetectViewModel,
  onDetectClick: () -> Unit,
  onBackClick: () -> Unit
) {
  composable(
    route = Routes.EDIT_PATTERN,
    arguments = listOf(
      navArgument(Routes.ARG_INSPECTION_REPORT_ID) {
        type = NavType.LongType
      }
    )
  ) { backStackEntry ->
    val inspectionReportId = backStackEntry.getOptionalInspectionReportId() ?: run {
      onBackClick()
      return@composable
    }

    val editedInspectionReportState =
      produceState<InspectionReport?>(initialValue = null, inspectionReportId) {
        inspectionReportsViewModel.getInspectionReportById(inspectionReportId) { inspectionReport ->
          value = inspectionReport
        }
      }

    val editedInspectionReport = editedInspectionReportState.value ?: return@composable

    CreateInspectionReportRoute(
      inspectionReportsViewModel = inspectionReportsViewModel,
      persistedAppStateViewModel = persistedAppStateViewModel,
      detectViewModel = detectViewModel,
      editedInspectionReport = editedInspectionReport,
      onDetectClick = onDetectClick,
      onBackClick = onBackClick
    )
  }
}

private fun NavGraphBuilder.viewInspectionReportRoute(
  inspectionReportsViewModel: InspectionReportsViewModel,
  persistedAppStateViewModel: PersistedAppStateViewModel,
  onBackClick: () -> Unit,
  onEditClick: (Long) -> Unit
) {
  composable(
    route = Routes.VIEW_PATTERN,
    arguments = listOf(
      navArgument(Routes.ARG_INSPECTION_REPORT_ID) {
        type = NavType.LongType
      }
    )
  ) { backStackEntry ->
    val inspectionReportId = backStackEntry.getOptionalInspectionReportId() ?: run {
      onBackClick()
      return@composable
    }

    val inspectionReportState =
      produceState<InspectionReport?>(initialValue = null, inspectionReportId) {
        inspectionReportsViewModel.getInspectionReportById(inspectionReportId) { inspectionReport ->
          value = inspectionReport
        }
      }

    ViewInspectionReportRoute(
      inspectionReport = inspectionReportState.value,
      persistedAppStateViewModel = persistedAppStateViewModel,
      onBackClick = onBackClick,
      onEditClick = onEditClick,
      onDeleteClick = { id ->
        inspectionReportsViewModel.deleteInspectionReport(id)
      }
    )
  }
}

private fun NavHostController.navigateToAuthRoute(
  route: String,
  authViewModel: AuthViewModel,
  clearMessage: Boolean = true
) {
  authViewModel.clearUserName()
  authViewModel.clearPassword()

  if (clearMessage) {
    authViewModel.clearMessage()
  }

  authViewModel.clearProfileAvatarImage()
  authViewModel.cancelSensitiveOperation()

  navigate(route) {
    launchSingleTop = true
  }
}