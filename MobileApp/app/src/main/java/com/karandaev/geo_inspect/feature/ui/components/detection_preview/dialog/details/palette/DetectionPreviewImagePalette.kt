package com.karandaev.geo_inspect.feature.ui.components.detection_preview.dialog.details.palette

/**
 * UI-ready image palette.
 *
 * @param colors Extracted representative image colors.
 */
internal data class DetectionPreviewImagePalette(
  val colors: List<DetectionPreviewPaletteColor>
) {

  /**
   * Returns whether palette contains at least one color.
   */
  val hasColors: Boolean
    get() = colors.isNotEmpty()
}

/**
 * UI-ready palette color.
 *
 * @param colorArgb Color ARGB int.
 * @param hexText Color formatted as HEX.
 */
internal data class DetectionPreviewPaletteColor(
  val colorArgb: Int,
  val hexText: String
)