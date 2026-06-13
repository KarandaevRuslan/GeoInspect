package com.karandaev.geo_inspect.app.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import com.karandaev.geo_inspect.app.components.AppNavigationRail
import com.karandaev.geo_inspect.app.navigation.AppDestination

@Composable
internal fun MapNotesTwoPaneScaffold(
  destination: AppDestination,
  onNavigateToTopLevelDestination: (String) -> Unit,
  onCreateNoteClick: () -> Unit,
  content: @Composable (Modifier) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxSize()
      .windowInsetsPadding(
        WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
      )
  ) {
    if (destination.showNavigation) {
      AppNavigationRail(
        currentRoute = destination.route,
        showCreateNoteButton = destination.showCreateInspectionReportFab,
        onCreateNoteClick = onCreateNoteClick,
        onNavigate = onNavigateToTopLevelDestination
      )
    }

    Scaffold(
      modifier = Modifier
        .weight(1f)
        .fillMaxSize(),
      contentWindowInsets = WindowInsets(
        left = 0.dp,
        top = 0.dp,
        right = 0.dp,
        bottom = 0.dp
      )
    ) { innerPadding ->
      Box(
        modifier = Modifier.fillMaxSize()
      ) {
        val contentModifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .windowInsetsPadding(
            WindowInsets.statusBars.only(WindowInsetsSides.Top)
          )
          .windowInsetsPadding(
            WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
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
}