package com.karandaev.geo_inspect.core.presentation.detection

/**
 * Current detection flow step.
 */
enum class DetectStep {

  /**
   * No image is selected yet.
   */
  ChooseSource,

  /**
   * Image is selected and user can crop it before running detection.
   */
  CropImage
}