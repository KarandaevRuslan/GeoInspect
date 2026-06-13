package com.karandaev.geo_inspect.core.inspection_api.mapper

import com.karandaev.geo_inspect.core.inspection_api.dto.detect.NormalizedBBoxDto
import com.karandaev.geo_inspect.core.inspection_api.dto.detect.DetectResponseDto
import com.karandaev.geo_inspect.core.inspection_api.dto.detect.DetectionDto
import com.karandaev.geo_inspect.core.domain.model.detection.NormalizedBBox
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.detection.Detection

/**
 * Maps road crack detection remote DTOs to app models.
 */
fun DetectResponseDto.toDetectResponse(): DetectResponse {
  return DetectResponse(
    detections = detections.map { detection ->
      detection.toDetection()
    }
  )
}

private fun DetectionDto.toDetection(): Detection {
  return Detection(
    clazz = clazz,
    confidence = confidence,
    bbox = bbox.toBBox()
  )
}

private fun NormalizedBBoxDto.toBBox(): NormalizedBBox {
  return NormalizedBBox(
    xMin = xMin,
    yMin = yMin,
    xMax = xMax,
    yMax = yMax
  )
}