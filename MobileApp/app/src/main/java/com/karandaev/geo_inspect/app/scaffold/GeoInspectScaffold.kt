package com.karandaev.geo_inspect.app.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.karandaev.geo_inspect.app.adaptive_layout.AdaptiveLayoutType
import com.karandaev.geo_inspect.app.navigation.AppDestination
import com.karandaev.geo_inspect.app.navigation.Routes

/**
 * Adaptive application scaffold coordinator.
 *
 * Chooses the appropriate root chrome for the current window size:
 * - bottom navigation for compact layouts;
 * - navigation rail for wide or landscape layouts.
 */
@Composable
fun GeoInspectScaffold(
  destination: AppDestination,
  layoutType: AdaptiveLayoutType,
  navController: NavHostController,
  content: @Composable (Modifier) -> Unit
) {
  val onNavigateToTopLevelDestination: (String) -> Unit = { route ->
    navController.navigateToTopLevelDestination(route)
  }

  val onCreateNoteClick = {
    navController.navigate(Routes.create())
  }

  when (layoutType) {
    AdaptiveLayoutType.SinglePane -> {
      MapNotesSinglePaneScaffold(
        destination = destination,
        onBackClick = navController::popBackStack,
        onNavigateToTopLevelDestination = onNavigateToTopLevelDestination,
        onCreateNoteClick = onCreateNoteClick,
        content = content
      )
    }

    AdaptiveLayoutType.TwoPane -> {
      MapNotesTwoPaneScaffold(
        destination = destination,
        onNavigateToTopLevelDestination = onNavigateToTopLevelDestination,
        onCreateNoteClick = onCreateNoteClick,
        content = content
      )
    }
  }
}

/**
 * Navigates to a top-level destination while preserving and restoring destination state.
 */
private fun NavHostController.navigateToTopLevelDestination(route: String) {
  navigate(route) {
    popUpTo(Routes.HOME) {
      saveState = true
    }

    launchSingleTop = true
    restoreState = true
  }
}