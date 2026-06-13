package com.karandaev.geo_inspect.core.inspection_api.dto.detect


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Remote response returned by the road crack detection API.
 *
 * @param imageWidth Source image width in pixels.
 * @param imageHeight Source image height in pixels.
 * @param imageFormat Source image format.
 * @param detections List of detected road cracks.
 */
@JsonClass(generateAdapter = true)
data class DetectResponseDto(
  @Json(name = "imageWidth")
  val imageWidth: Int,

  @Json(name = "imageHeight")
  val imageHeight: Int,

  @Json(name = "imageFormat")
  val imageFormat: String,

  @Json(name = "detections")
  val detections: List<DetectionDto> = emptyList()
)