package com.karandaev.geo_inspect.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun AppBackButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  IconButton(
    onClick = onClick,
    modifier = modifier
      .clip(CircleShape)
      .background(MaterialTheme.colorScheme.surface)
  ) {
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowBack,
      contentDescription = "Back",
      tint = MaterialTheme.colorScheme.onSurface
    )
  }
}