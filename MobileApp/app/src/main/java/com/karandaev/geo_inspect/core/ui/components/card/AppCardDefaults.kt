package com.karandaev.geo_inspect.core.ui.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

sealed interface AppCardBackground {

  data object Default : AppCardBackground

  data class Color(
    val value: androidx.compose.ui.graphics.Color
  ) : AppCardBackground

  data class Brush(
    val value: androidx.compose.ui.graphics.Brush
  ) : AppCardBackground
}

/**
 * Defaults for reusable app cards.
 */
object AppCardDefaults {

  val shape: Shape
    get() = RoundedCornerShape(12.dp)

  val contentPadding: PaddingValues
    get() = PaddingValues(16.dp)

  @Composable
  fun colors(): CardColors {
    return CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  }

  @Composable
  fun tonalColors(): CardColors {
    return CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
  }

  @Composable
  fun transparentColors(): CardColors {
    return CardDefaults.cardColors(
      containerColor = Color.Transparent
    )
  }

  @Composable
  fun border(): BorderStroke {
    return BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.outlineVariant
    )
  }
}