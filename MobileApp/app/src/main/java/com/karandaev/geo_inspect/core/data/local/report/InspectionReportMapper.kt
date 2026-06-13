package com.karandaev.geo_inspect.core.data.local.report

import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.detection.Detection
import com.karandaev.geo_inspect.core.domain.model.detection.NormalizedBBox
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Converts a local report entity with detections to a domain report model.
 */
fun InspectionReportWithDetections.toDomain(): InspectionReport {
  val sortedDetections = detections.sortedBy { detection ->
    detection.sortOrder
  }

  return report.toDomain(
    detectResponse = sortedDetections
      .takeIf { it.isNotEmpty() }
      ?.let { detectionEntities ->
        DetectResponse(
          detections = detectionEntities.map { detection -> detection.toDomain() }
        )
      }
  )
}

/**
 * Converts a local report entity to a domain report model.
 */
fun InspectionReportEntity.toDomain(
  detectResponse: DetectResponse? = null
): InspectionReport {
  return InspectionReport(
    id = id,
    title = title,
    content = content,
    lastUpdatedAtMillis = lastUpdatedAtMillis,
    point = MapPoint(
      latitude = latitude,
      longitude = longitude
    ),
    detectResponse = detectResponse
  )
}

/**
 * Converts a domain report model to a local report entity.
 */
fun InspectionReport.toEntity(): InspectionReportEntity {
  return InspectionReportEntity(
    id = id,
    title = title,
    content = content,
    lastUpdatedAtMillis = lastUpdatedAtMillis,
    latitude = latitude,
    longitude = longitude
  )
}

/**
 * Converts a domain report model to a local Room relation model.
 */
fun InspectionReport.toEntityWithDetections(): InspectionReportWithDetections {
  return InspectionReportWithDetections(
    report = toEntity(),
    detections = detectResponse.toDetectionEntities(
      inspectionReportId = id
    )
  )
}

/**
 * Converts a domain detection response to local detection entities.
 */
fun DetectResponse?.toDetectionEntities(
  inspectionReportId: Long
): List<DetectionEntity> {
  return this?.detections
    ?.mapIndexed { index, detection ->
      detection.toEntity(
        inspectionReportId = inspectionReportId,
        sortOrder = index
      )
    }
    .orEmpty()
}

/**
 * Converts a local detection entity to a domain detection model.
 */
fun DetectionEntity.toDomain(): Detection {
  return Detection(
    clazz = clazz,
    confidence = confidence,
    bbox = NormalizedBBox(
      xMin = xMin,
      yMin = yMin,
      xMax = xMax,
      yMax = yMax
    )
  )
}

/**
 * Converts a domain detection model to a local detection entity.
 */
fun Detection.toEntity(
  inspectionReportId: Long,
  sortOrder: Int
): DetectionEntity {
  return DetectionEntity(
    inspectionReportId = inspectionReportId,
    sortOrder = sortOrder,
    clazz = clazz,
    confidence = confidence,
    xMin = bbox.xMin,
    yMin = bbox.yMin,
    xMax = bbox.xMax,
    yMax = bbox.yMax
  )
}