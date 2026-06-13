package com.karandaev.geo_inspect.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.feature.presentation.auth.model.AuthMode
import com.karandaev.geo_inspect.feature.presentation.auth.navigation.isAuth
import com.karandaev.geo_inspect.feature.presentation.auth.navigation.toAuthModeOrNull

/**
 * Describes app-level UI chrome for the current navigation destination.
 *
 * This model keeps route-specific scaffold decisions, such as top bar visibility,
 * app navigation visibility, title, and FAB visibility, outside of the root composable.
 */
data class AppDestination(
  val route: String?,
  val title: String,
  val showTopBar: Boolean,
  val showBackButton: Boolean,
  val showNavigation: Boolean,
  val showCreateInspectionReportFab: Boolean,
  val protectStatusBar: Boolean
)

@Composable
fun rememberAppDestination(
  navBackStackEntry: NavBackStackEntry?,
  inspectionReports: List<InspectionReport>
): AppDestination {
  val route = navBackStackEntry?.destination?.route
  val inspectionReportId = navBackStackEntry?.getOptionalInspectionReportId()

  val title = resolveDestinationTitle(
    route = route,
    inspectionReportId = inspectionReportId,
    inspectionReports = inspectionReports
  )

  return remember(
    route,
    inspectionReportId,
    inspectionReports,
    title
  ) {
    val isTopLevelRoute = route.isTopLevelDestination()
    val isMapRoute = route == Routes.MAP
    val isHomeRoute = route == Routes.HOME
    val isAuthRoute = route.isAuth()

    AppDestination(
      route = route,
      title = title,
      showTopBar = !isMapRoute,
      showBackButton = !isTopLevelRoute && !isAuthRoute,
      showNavigation = isTopLevelRoute && !isAuthRoute,
      showCreateInspectionReportFab = isHomeRoute,
      protectStatusBar = isMapRoute
    )
  }
}

@Composable
private fun resolveDestinationTitle(
  route: String?,
  inspectionReportId: Long?,
  inspectionReports: List<InspectionReport>
): String {
  route.toAuthModeOrNull()?.let { mode ->
    return mode.title()
  }

  return when {
    route == Routes.HOME -> stringResource(R.string.destination_home)
    route == Routes.MAP -> stringResource(R.string.destination_map)
    route == Routes.DETECT -> stringResource(R.string.destination_detection)
    route == Routes.PROFILE -> stringResource(R.string.destination_profile)
    route == Routes.SETTINGS -> stringResource(R.string.destination_settings)
    route?.startsWith(Routes.CREATE) == true -> {
      stringResource(R.string.destination_create_inspection_report)
    }

    route?.startsWith("${Routes.EDIT}/") == true -> {
      stringResource(R.string.destination_edit_inspection_report)
    }

    route?.startsWith("${Routes.VIEW}/") == true -> {
      inspectionReports.firstOrNull { note -> note.id == inspectionReportId }?.title
        ?: stringResource(R.string.destination_view_inspection_report_fallback)
    }

    else -> stringResource(R.string.app_name)
  }
}

@Composable
private fun AuthMode.title(): String {
  return when (this) {
    AuthMode.Login -> stringResource(R.string.destination_auth_login)
    AuthMode.Register -> stringResource(R.string.destination_auth_register)
    AuthMode.PasswordReset -> stringResource(R.string.destination_auth_password_reset)
  }
}