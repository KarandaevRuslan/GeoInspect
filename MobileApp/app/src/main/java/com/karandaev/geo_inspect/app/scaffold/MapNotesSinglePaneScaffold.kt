package com.karandaev.geo_inspect.app.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.app.components.AppNavigationBar
import com.karandaev.geo_inspect.app.components.AppTopBar
import com.karandaev.geo_inspect.app.components.CreateNoteFab
import com.karandaev.geo_inspect.app.navigation.AppDestination

@Composable
internal fun MapNotesSinglePaneScaffold(
  destination: AppDestination,
  onBackClick: () -> Unit,
  onNavigateToTopLevelDestination: (String) -> Unit,
  onCreateNoteClick: () -> Unit,
  content: @Composable (Modifier) -> Unit
) {
  Scaffold(
    contentWindowInsets = WindowInsets(
      left = 0.dp,
      top = 0.dp,
      right = 0.dp,
      bottom = 0.dp
    ),
    topBar = {
      if (destination.showTopBar) {
        AppTopBar(
          title = destination.title,
          showBackButton = destination.showBackButton,
          onBackClick = onBackClick
        )
      }
    },
    bottomBar = {
      if (destination.showNavigation) {
        AppNavigationBar(
          currentRoute = destination.route,
          onNavigate = onNavigateToTopLevelDestination
        )
      }
    },
    floatingActionButton = {
      if (destination.showCreateInspectionReportFab) {
        CreateNoteFab(
          onClick = onCreateNoteClick
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier.fillMaxSize()
    ) {
      val contentModifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .then(
          if (destination.protectStatusBar) {
            Modifier.windowInsetsPadding(
              WindowInsets.statusBars.only(WindowInsetsSides.Top)
            )
          } else {
            Modifier
          }
        )
        .then(
          if (!destination.showNavigation) {
            Modifier.windowInsetsPadding(
              WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
            )
          } else {
            Modifier
          }
        )

      content(contentModifier)

      if (destination.protectStatusBar) {
        Box(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(MaterialTheme.colorScheme.surface)
        )
      }
    }
  }
}