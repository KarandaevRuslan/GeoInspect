package com.karandaev.geo_inspect.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Describes a top-level app destination shown in the bottom navigation bar.
 *
 * Top-level destinations do not show the back button and are restored when switching
 * between bottom navigation tabs.
 */
data class TopLevelDestination(
  val route: String,
  val label: String,
  val icon: ImageVector
)

/**
 * Top-level destinations available from the bottom navigation bar.
 */
val TopLevelDestinations = listOf(
  TopLevelDestination(
    route = Routes.HOME,
    label = "Reports",
    icon = Icons.Default.Description
  ),
  TopLevelDestination(
    route = Routes.MAP,
    label = "Map",
    icon = Icons.Default.Map
  ),
  TopLevelDestination(
    route = Routes.SETTINGS,
    label = "Settings",
    icon = Icons.Default.Settings
  )
)

/**
 * Checks whether the route belongs to a top-level destination.
 */
fun String?.isTopLevelDestination(): Boolean {
  return TopLevelDestinations.any { destination ->
    destination.route == this
  }
}