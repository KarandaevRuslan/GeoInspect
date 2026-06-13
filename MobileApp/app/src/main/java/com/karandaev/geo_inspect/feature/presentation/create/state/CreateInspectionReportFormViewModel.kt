package com.karandaev.geo_inspect.feature.presentation.create.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.util.mappers.toMapPoint
import com.karandaev.geo_inspect.core.util.other.withLatitude
import com.karandaev.geo_inspect.core.util.other.withLongitude

/**
 * ViewModel-backed mutable UI state for the create/edit inspection report form.
 *
 * The state is backed by [SavedStateHandle], so it survives configuration changes
 * and keeps one shared form state between portrait and landscape layouts.
 *
 * This ViewModel stores only form-level accepted detection data. It does not run detection itself.
 */
class CreateInspectionReportFormViewModel internal constructor(
  private val savedStateHandle: SavedStateHandle,
  initialSelectedPoint: MapPoint?
) : ViewModel() {

  // -----------------------------------------------------------------------------------------------
  // Initialization
  // -----------------------------------------------------------------------------------------------

  init {
    initializeIfNeeded(initialSelectedPoint)
  }

  // -----------------------------------------------------------------------------------------------
  // Text form state
  // -----------------------------------------------------------------------------------------------

  var title by mutableStateOf(
    savedStateHandle.get<String>(CreateInspectionReportFormSavedStateKeys.TITLE).orEmpty()
  )
    private set

  var content by mutableStateOf(
    savedStateHandle.get<String>(CreateInspectionReportFormSavedStateKeys.CONTENT).orEmpty()
  )
    private set

  // -----------------------------------------------------------------------------------------------
  // Detection form state
  // -----------------------------------------------------------------------------------------------

  var detectionImagePath by mutableStateOf<String?>(
    savedStateHandle.get<String>(CreateInspectionReportFormSavedStateKeys.DETECTION_IMAGE_PATH)
  )
    private set

  var isDetectionMarkedForRemoval by mutableStateOf(
    savedStateHandle.get<Boolean>(
      CreateInspectionReportFormSavedStateKeys.DETECTION_MARKED_FOR_REMOVAL
    ) ?: false
  )
    private set

  var detectResponse by mutableStateOf<DetectResponse?>(null)
    private set

  private var appliedDetectionRefreshId by mutableLongStateOf(
    savedStateHandle.get<Long>(
      CreateInspectionReportFormSavedStateKeys.APPLIED_DETECTION_REFRESH_ID
    ) ?: 0L
  )

  // -----------------------------------------------------------------------------------------------
  // Location form state
  // -----------------------------------------------------------------------------------------------

  var selectedPoint by mutableStateOf(
    restoreSelectedPoint()
  )
    private set

  var mapFocusRequest by mutableIntStateOf(
    savedStateHandle.get<Int>(
      CreateInspectionReportFormSavedStateKeys.MAP_FOCUS_REQUEST
    ) ?: 0
  )
    private set

  // -----------------------------------------------------------------------------------------------
  // Loaded inspection report guard
  // -----------------------------------------------------------------------------------------------

  private var loadedInspectionReportWasApplied: Boolean
    get() = savedStateHandle[
      CreateInspectionReportFormSavedStateKeys.LOADED_INSPECTION_REPORT_WAS_APPLIED
    ] ?: false
    set(value) {
      savedStateHandle[
        CreateInspectionReportFormSavedStateKeys.LOADED_INSPECTION_REPORT_WAS_APPLIED
      ] = value
    }

  // -----------------------------------------------------------------------------------------------
  // Text actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Updates report title draft.
   *
   * @param value New title value.
   */
  fun onTitleChange(value: String) {
    title = value
    savedStateHandle[CreateInspectionReportFormSavedStateKeys.TITLE] = value
  }

  /**
   * Updates report content draft.
   *
   * @param value New content value.
   */
  fun onContentChange(value: String) {
    content = value
    savedStateHandle[CreateInspectionReportFormSavedStateKeys.CONTENT] = value
  }

  // -----------------------------------------------------------------------------------------------
  // Location actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Updates selected map point.
   *
   * @param point New selected point.
   */
  fun onSelectedPointChange(point: MapPoint) {
    selectedPoint = point
    saveSelectedPoint(point)
  }

  /**
   * Updates selected point latitude.
   *
   * @param latitude New latitude.
   * @param fallbackLongitude Longitude used when selected point is empty.
   */
  fun onLatitudeChange(
    latitude: Double,
    fallbackLongitude: Double
  ) {
    val updatedPoint = selectedPoint.withLatitude(
      latitude = latitude,
      fallbackLongitude = fallbackLongitude
    )

    selectedPoint = updatedPoint
    saveSelectedPoint(updatedPoint)
  }

  /**
   * Updates selected point longitude.
   *
   * @param longitude New longitude.
   * @param fallbackLatitude Latitude used when selected point is empty.
   */
  fun onLongitudeChange(
    longitude: Double,
    fallbackLatitude: Double
  ) {
    val updatedPoint = selectedPoint.withLongitude(
      longitude = longitude,
      fallbackLatitude = fallbackLatitude
    )

    selectedPoint = updatedPoint
    saveSelectedPoint(updatedPoint)
  }

  /**
   * Clears selected point and requests focusing current device location on the map.
   */
  fun useCurrentLocation() {
    clearSelectedPoint()
    requestMapFocus()
  }

  /**
   * Requests map focus by increasing focus request counter.
   */
  fun requestMapFocus() {
    mapFocusRequest = nextMapFocusRequest()

    savedStateHandle[CreateInspectionReportFormSavedStateKeys.MAP_FOCUS_REQUEST] =
      mapFocusRequest
  }

  // -----------------------------------------------------------------------------------------------
  // Loaded inspection report actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Applies loaded inspection report to the form once.
   *
   * Used by edit mode after report data is loaded asynchronously.
   *
   * @param inspectionReport Loaded inspection report, or null while loading.
   * @param detectionImagePath Existing stored detection image path.
   */
  fun applyLoadedInspectionReportIfNeeded(
    inspectionReport: InspectionReport?,
    detectionImagePath: String?
  ) {
    if (!canApplyLoadedInspectionReport(inspectionReport)) {
      return
    }

    applyLoadedInspectionReport(
      inspectionReport = inspectionReport!!,
      detectionImagePath = detectionImagePath
    )

    markLoadedInspectionReportApplied()
    requestMapFocus()
  }

  // -----------------------------------------------------------------------------------------------
  // Detection actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Applies completed detection result to the create/edit form only once.
   *
   * A newly accepted detection cancels pending detection removal.
   */
  fun applyDetectionResultIfNew(
    refreshId: Long,
    imagePath: String,
    detectResponse: DetectResponse
  ) {
    if (refreshId <= appliedDetectionRefreshId) {
      return
    }

    detectionImagePath = imagePath
    this.detectResponse = detectResponse
    appliedDetectionRefreshId = refreshId

    saveDetectionImagePath(imagePath)
    updateDetectionMarkedForRemoval(false)

    savedStateHandle[CreateInspectionReportFormSavedStateKeys.APPLIED_DETECTION_REFRESH_ID] =
      refreshId
  }

  /**
   * Clears accepted detection result from the form draft.
   *
   * This does not delete stored image immediately. In edit mode the stored image is removed
   * only after user saves the form.
   */
  fun clearDetectionResult() {
    detectionImagePath = null
    detectResponse = null
    appliedDetectionRefreshId = 0L

    saveDetectionImagePath(null)
    updateDetectionMarkedForRemoval(true)

    savedStateHandle[CreateInspectionReportFormSavedStateKeys.APPLIED_DETECTION_REFRESH_ID] =
      0L
  }

  // -----------------------------------------------------------------------------------------------
  // Detection helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Saves draft detection removal flag.
   *
   * The actual image file is not deleted here. Physical deletion happens only on save.
   */
  private fun updateDetectionMarkedForRemoval(value: Boolean) {
    isDetectionMarkedForRemoval = value

    savedStateHandle[CreateInspectionReportFormSavedStateKeys.DETECTION_MARKED_FOR_REMOVAL] =
      value
  }

  // -----------------------------------------------------------------------------------------------
  // Derived state
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns point that should be used for saving.
   *
   * Selected point has priority over current device location.
   *
   * @param currentLocationPoint Current device location point.
   */
  fun effectivePoint(
    currentLocationPoint: MapPoint?
  ): MapPoint? {
    return selectedPoint ?: currentLocationPoint
  }

  // -----------------------------------------------------------------------------------------------
  // Initialization helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Initializes selected point once for this form instance.
   *
   * @param initialSelectedPoint Initial point from route args.
   */
  private fun initializeIfNeeded(
    initialSelectedPoint: MapPoint?
  ) {
    val wasInitialized =
      savedStateHandle[CreateInspectionReportFormSavedStateKeys.INITIALIZED] ?: false

    if (wasInitialized) {
      return
    }

    saveSelectedPoint(initialSelectedPoint)
    savedStateHandle[CreateInspectionReportFormSavedStateKeys.INITIALIZED] = true
  }

  /**
   * Returns whether loaded inspection report can be applied to the form.
   */
  private fun canApplyLoadedInspectionReport(
    inspectionReport: InspectionReport?
  ): Boolean {
    return inspectionReport != null && !loadedInspectionReportWasApplied
  }

  /**
   * Applies loaded inspection report fields to form state.
   */
  private fun applyLoadedInspectionReport(
    inspectionReport: InspectionReport,
    detectionImagePath: String?
  ) {
    applyLoadedInspectionReportText(
      inspectionReport = inspectionReport
    )

    applyLoadedInspectionReportLocation(
      inspectionReport = inspectionReport
    )

    applyLoadedInspectionReportDetection(
      inspectionReport = inspectionReport,
      detectionImagePath = detectionImagePath
    )
  }

  /**
   * Applies loaded report title and content to form state.
   */
  private fun applyLoadedInspectionReportText(
    inspectionReport: InspectionReport
  ) {
    title = inspectionReport.title
    content = inspectionReport.content

    savedStateHandle[CreateInspectionReportFormSavedStateKeys.TITLE] = title
    savedStateHandle[CreateInspectionReportFormSavedStateKeys.CONTENT] = content
  }

  /**
   * Applies loaded report map point to form state.
   */
  private fun applyLoadedInspectionReportLocation(
    inspectionReport: InspectionReport
  ) {
    selectedPoint = inspectionReport.toMapPoint()
    saveSelectedPoint(selectedPoint)
  }

  /**
   * Applies loaded report detection data to form state.
   *
   * Existing report detection becomes editable draft state.
   */
  private fun applyLoadedInspectionReportDetection(
    inspectionReport: InspectionReport,
    detectionImagePath: String?
  ) {
    this.detectionImagePath = detectionImagePath
    this.detectResponse = inspectionReport.detectResponse

    saveDetectionImagePath(detectionImagePath)
    updateDetectionMarkedForRemoval(false)
  }

  /**
   * Saves accepted detection image path into [SavedStateHandle].
   */
  private fun saveDetectionImagePath(
    imagePath: String?
  ) {
    if (imagePath.isNullOrBlank()) {
      savedStateHandle.remove<String>(
        CreateInspectionReportFormSavedStateKeys.DETECTION_IMAGE_PATH
      )
      return
    }

    savedStateHandle[CreateInspectionReportFormSavedStateKeys.DETECTION_IMAGE_PATH] =
      imagePath
  }

  /**
   * Marks loaded inspection report as already applied.
   */
  private fun markLoadedInspectionReportApplied() {
    loadedInspectionReportWasApplied = true
  }

  // -----------------------------------------------------------------------------------------------
  // Selected point helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Clears selected point.
   */
  private fun clearSelectedPoint() {
    selectedPoint = null
    saveSelectedPoint(null)
  }

  /**
   * Returns next map focus request id.
   */
  private fun nextMapFocusRequest(): Int {
    return mapFocusRequest + 1
  }

  /**
   * Saves selected point into [SavedStateHandle].
   *
   * @param point Selected point, or null when current location should be used.
   */
  private fun saveSelectedPoint(
    point: MapPoint?
  ) {
    savedStateHandle[CreateInspectionReportFormSavedStateKeys.HAS_SELECTED_POINT] =
      point != null

    if (point == null) {
      savedStateHandle.remove<Double>(
        CreateInspectionReportFormSavedStateKeys.SELECTED_POINT_LATITUDE
      )

      savedStateHandle.remove<Double>(
        CreateInspectionReportFormSavedStateKeys.SELECTED_POINT_LONGITUDE
      )

      return
    }

    savedStateHandle[CreateInspectionReportFormSavedStateKeys.SELECTED_POINT_LATITUDE] =
      point.latitude

    savedStateHandle[CreateInspectionReportFormSavedStateKeys.SELECTED_POINT_LONGITUDE] =
      point.longitude
  }

  /**
   * Restores selected point from [SavedStateHandle].
   */
  private fun restoreSelectedPoint(): MapPoint? {
    val hasSelectedPoint =
      savedStateHandle[CreateInspectionReportFormSavedStateKeys.HAS_SELECTED_POINT] ?: false

    if (!hasSelectedPoint) {
      return null
    }

    val latitude = savedStateHandle.get<Double>(
      CreateInspectionReportFormSavedStateKeys.SELECTED_POINT_LATITUDE
    ) ?: return null

    val longitude = savedStateHandle.get<Double>(
      CreateInspectionReportFormSavedStateKeys.SELECTED_POINT_LONGITUDE
    ) ?: return null

    return MapPoint(
      latitude = latitude,
      longitude = longitude
    )
  }
}