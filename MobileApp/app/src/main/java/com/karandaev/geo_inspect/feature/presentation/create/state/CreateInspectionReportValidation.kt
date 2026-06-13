package com.karandaev.geo_inspect.feature.presentation.create.state

import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint

/**
 * Checks whether the current form can be saved.
 *
 * @param title Current title value.
 * @param effectivePoint Selected point or current location point.
 * @param isEditMode Whether the screen is opened in edit mode.
 * @param editedInspectionReport Existing note when edit mode is active.
 * @return True when save action should be enabled.
 */
internal fun canSaveCreateInspectionReport(
  title: String,
  effectivePoint: MapPoint?,
  isEditMode: Boolean,
  editedInspectionReport: InspectionReport?
): Boolean {
  return title.isNotBlank() &&
    effectivePoint != null &&
    (!isEditMode || editedInspectionReport != null)
}