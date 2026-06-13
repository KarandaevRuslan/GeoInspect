package com.karandaev.geo_inspect.core.data.local.report

/**
 * Provides initial inspection reports inserted when the database is created for the first time.
 */
object DefaultInspectionReportsSeed {

  /**
   * Creates default inspection report entities.
   */
  fun create(): List<InspectionReportWithDetections> {
    val now = System.currentTimeMillis()

    return listOf(
      createReport(
        title = "Road crack on Main Street",
        content = "Longitudinal asphalt crack detected near the right traffic lane.",
        lastUpdatedAtMillis = now - 3L * DayMillis,
        latitude = 51.5055,
        longitude = -0.0754,
        detections = listOf(
          createDetection(
            sortOrder = 0,
            clazz = "longitudinal_crack",
            confidence = 0.91,
            xMin = 0.18,
            yMin = 0.42,
            xMax = 0.76,
            yMax = 0.58
          )
        )
      ),
      createReport(
        title = "Pothole near bus stop",
        content = "Surface damage found close to the curb. Requires maintenance inspection.",
        lastUpdatedAtMillis = now - 2L * DayMillis,
        latitude = 51.5194,
        longitude = -0.1270,
        detections = listOf(
          createDetection(
            sortOrder = 0,
            clazz = "pothole",
            confidence = 0.87,
            xMin = 0.35,
            yMin = 0.50,
            xMax = 0.62,
            yMax = 0.74
          )
        )
      ),
      createReport(
        title = "Multiple pavement defects",
        content = "Several damaged asphalt areas detected on the road segment.",
        lastUpdatedAtMillis = now - DayMillis,
        latitude = 51.5073,
        longitude = -0.1657,
        detections = listOf(
          createDetection(
            sortOrder = 0,
            clazz = "transverse_crack",
            confidence = 0.82,
            xMin = 0.12,
            yMin = 0.30,
            xMax = 0.86,
            yMax = 0.41
          ),
          createDetection(
            sortOrder = 1,
            clazz = "alligator_crack",
            confidence = 0.79,
            xMin = 0.24,
            yMin = 0.56,
            xMax = 0.68,
            yMax = 0.84
          )
        )
      ),
      createReport(
        title = "Inspection point without detection",
        content = "Manual report created for a road segment that still needs image-based detection.",
        lastUpdatedAtMillis = now,
        latitude = 51.5033,
        longitude = -0.1196,
        detections = emptyList()
      )
    )
  }

  private fun createReport(
    title: String,
    content: String,
    lastUpdatedAtMillis: Long,
    latitude: Double,
    longitude: Double,
    detections: List<DetectionEntity>
  ): InspectionReportWithDetections {
    return InspectionReportWithDetections(
      report = InspectionReportEntity(
        title = title,
        content = content,
        lastUpdatedAtMillis = lastUpdatedAtMillis,
        latitude = latitude,
        longitude = longitude
      ),
      detections = detections
    )
  }

  private fun createDetection(
    sortOrder: Int,
    clazz: String,
    confidence: Double,
    xMin: Double,
    yMin: Double,
    xMax: Double,
    yMax: Double
  ): DetectionEntity {
    return DetectionEntity(
      inspectionReportId = 0,
      sortOrder = sortOrder,
      clazz = clazz,
      confidence = confidence,
      xMin = xMin,
      yMin = yMin,
      xMax = xMax,
      yMax = yMax
    )
  }

  private const val DayMillis = 24L * 60L * 60L * 1000L
}