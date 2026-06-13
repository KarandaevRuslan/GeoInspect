package com.karandaev.geo_inspect.core.image.crop

/**
 * Normalized crop box.
 *
 * Coordinates are stored in normalized form in the 0.0..1.0 range.
 */
data class NormalizedCropBox(
  val xMin: Double,
  val yMin: Double,
  val xMax: Double,
  val yMax: Double
) {

  init {
    require(xMin in 0.0..1.0) { "xMin must be in 0.0..1.0." }
    require(yMin in 0.0..1.0) { "yMin must be in 0.0..1.0." }
    require(xMax in 0.0..1.0) { "xMax must be in 0.0..1.0." }
    require(yMax in 0.0..1.0) { "yMax must be in 0.0..1.0." }
    require(xMin < xMax) { "xMin must be less than xMax." }
    require(yMin < yMax) { "yMin must be less than yMax." }
  }

  companion object {

    /**
     * Creates a normalized crop box from pixel coordinates.
     */
    fun fromPixels(
      xMin: Int,
      yMin: Int,
      xMax: Int,
      yMax: Int,
      imageWidth: Int,
      imageHeight: Int
    ): NormalizedCropBox {
      require(imageWidth > 0) { "Image width must be positive." }
      require(imageHeight > 0) { "Image height must be positive." }

      return NormalizedCropBox(
        xMin = xMin.toDouble() / imageWidth.toDouble(),
        yMin = yMin.toDouble() / imageHeight.toDouble(),
        xMax = xMax.toDouble() / imageWidth.toDouble(),
        yMax = yMax.toDouble() / imageHeight.toDouble()
      )
    }
  }
}