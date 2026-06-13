package com.karandaev.geo_inspect.core.data.json.report

import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Converts a JSON report model to a domain report model.
 */
fun InspectionReportJson.toDomain(): InspectionReport {
  return InspectionReport(
    id = id,
    title = title,
    content = content,
    lastUpdatedAtMillis = lastUpdatedAtMillis,
    point = point.toDomain(),
    detectResponse = detectResponse?.toDomain()
  )
}

/**
 * Converts a domain report model to a JSON report model.
 */
fun InspectionReport.toJson(): InspectionReportJson {
  return InspectionReportJson(
    id = id,
    title = title,
    content = content,
    lastUpdatedAtMillis = lastUpdatedAtMillis,
    point = point.toJson(),
    detectResponse = detectResponse?.toJson()
  )
}

/**
 * Converts a JSON map point model to a domain map point model.
 */
fun MapPointJson.toDomain(): MapPoint {
  return MapPoint(
    latitude = latitude,
    longitude = longitude
  )
}

/**
 * Converts a domain map point model to a JSON map point model.
 */
fun MapPoint.toJson(): MapPointJson {
  return MapPointJson(
    latitude = latitude,
    longitude = longitude
  )
}