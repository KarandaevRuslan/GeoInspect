package com.karandaev.geo_inspect.core.data.json.report

import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import com.karandaev.geo_inspect.core.domain.model.detection.NormalizedBBox

/**
 * Converts a JSON detect response model to a domain detect response model.
 */
fun DetectResponseJson.toDomain(): DetectResponse {
  return DetectResponse(
    detections = detections.map { detection -> detection.toDomain() }
  )
}

/**
 * Converts a domain detect response model to a JSON detect response model.
 */
fun DetectResponse.toJson(): DetectResponseJson {
  return DetectResponseJson(
    detections = detections.map { detection -> detection.toJson() }
  )
}

/**
 * Converts a JSON detection model to a domain detection model.
 */
fun DetectionJson.toDomain(): Detection {
  return Detection(
    clazz = clazz,
    confidence = confidence,
    bbox = bbox.toDomain()
  )
}

/**
 * Converts a domain detection model to a JSON detection model.
 */
fun Detection.toJson(): DetectionJson {
  return DetectionJson(
    clazz = clazz,
    confidence = confidence,
    bbox = bbox.toJson()
  )
}

/**
 * Converts a JSON bounding box model to a domain bounding box model.
 */
fun NormalizedBBoxJson.toDomain(): NormalizedBBox {
  return NormalizedBBox(
    xMin = xMin,
    yMin = yMin,
    xMax = xMax,
    yMax = yMax
  )
}

/**
 * Converts a domain bounding box model to a JSON bounding box model.
 */
fun NormalizedBBox.toJson(): NormalizedBBoxJson {
  return NormalizedBBoxJson(
    xMin = xMin,
    yMin = yMin,
    xMax = xMax,
    yMax = yMax
  )
}