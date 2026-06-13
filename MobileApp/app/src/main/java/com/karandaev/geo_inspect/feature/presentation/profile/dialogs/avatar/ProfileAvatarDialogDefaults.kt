package com.karandaev.geo_inspect.feature.presentation.profile.dialogs.avatar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

internal const val ProfileAvatarLandscapeDialogWidthFraction = 0.9f
internal const val ProfileAvatarLandscapePreviewWeight = 1f
internal const val ProfileAvatarLandscapeHintWeight = 0.45f
internal const val ProfileAvatarLandscapeCropperWeight = 0.9f
internal const val ProfileAvatarLandscapeActionsWeight = 0.72f

internal val ProfileAvatarLandscapeCropperSize = 180.dp
internal val ProfileAvatarLandscapeSpacing = 16.dp
internal val ProfileAvatarPortraitSpacing = 12.dp
internal val ProfileAvatarContentSpacing = 8.dp

/**
 * Applies wider dialog width in landscape.
 */
internal fun Modifier.profileAvatarDialogWidth(
  isLandscape: Boolean
): Modifier {
  return if (isLandscape) {
    fillMaxWidth(ProfileAvatarLandscapeDialogWidthFraction)
  } else {
    this
  }
}