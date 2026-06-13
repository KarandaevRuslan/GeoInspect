package com.karandaev.geo_inspect.feature.presentation.detection.components.crop.cropper

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

internal val DetectCropperHeight = 320.dp
internal val DetectCropperBorderWidth = 2.dp
internal val DetectCropperContentSpacing = 8.dp
internal val DetectCropperTonalElevation = 2.dp
internal val DetectCropperShadowElevation = 1.dp

internal val DetectCropperShape = RoundedCornerShape(16.dp)

internal const val DetectCropperMinScale = 1f
internal const val DetectCropperMaxScale = 5f
internal const val DetectCropperScrimAlpha = 0.08f

internal const val DetectCropperContentDescription = "Image crop area"
internal const val DetectCropperImageLoadErrorText = "Image could not be loaded."
internal const val DetectCropperGestureHintText = "Drag to move, pinch to zoom."