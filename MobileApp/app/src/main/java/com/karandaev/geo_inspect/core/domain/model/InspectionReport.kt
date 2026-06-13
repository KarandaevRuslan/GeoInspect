package com.karandaev.geo_inspect.core.domain.model

import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Domain model representing an inspection report linked to a geographic point.
 *
 * This model is independent from local database entities and can be safely used
 * by the UI, ViewModel, and domain layers.
 */
data class InspectionReport(
  val id: Long = 0,
  val title: String,
  val content: String,
  val lastUpdatedAtMillis: Long,
  val point: MapPoint,
  val detectResponse: DetectResponse? = null
) {

  /**
   * Latitude of the report point.
   *
   * Kept for compatibility with code that has not migrated to [point] yet.
   */
  val latitude: Double
    get() = point.latitude

  /**
   * Longitude of the report point.
   *
   * Kept for compatibility with code that has not migrated to [point] yet.
   */
  val longitude: Double
    get() = point.longitude
}