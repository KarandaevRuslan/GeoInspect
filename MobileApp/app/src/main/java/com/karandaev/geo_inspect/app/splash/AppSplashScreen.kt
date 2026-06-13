package com.karandaev.geo_inspect.app.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

/**
 * Full-screen splash shown while app navigation and persisted access state are loading.
 */
@Composable
fun AppSplashScreen(
  modifier: Modifier = Modifier
) {
  val backgroundGradient = Brush.verticalGradient(
    colors = listOf(
      MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.32f),
      MaterialTheme.colorScheme.background,
      MaterialTheme.colorScheme.surface
    )
  )

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(backgroundGradient)
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        Box(
          modifier = Modifier
            .size(116.dp)
            .shadow(
              elevation = 18.dp,
              shape = RoundedCornerShape(30.dp),
              clip = false
            )
            .clip(RoundedCornerShape(30.dp))
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                  MaterialTheme.colorScheme.surface
                )
              )
            )
            .border(
              width = 1.dp,
              color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
              shape = RoundedCornerShape(30.dp)
            )
            .padding(14.dp),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
          )
        }

        Text(
          text = stringResource(id = R.string.app_name),
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )

        CircularProgressIndicator(
          modifier = Modifier.size(32.dp),
          strokeWidth = 3.dp,
          color = MaterialTheme.colorScheme.primary,
          trackColor = MaterialTheme.colorScheme.primaryContainer
        )
      }
    }
  }
}