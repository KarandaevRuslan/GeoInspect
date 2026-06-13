package com.karandaev.geo_inspect.feature.presentation.create.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.karandaev.geo_inspect.core.presentation.detection.DetectCompletedResult
import com.karandaev.geo_inspect.feature.presentation.create.state.CreateInspectionReportFormViewModel

/**
 * Copies completed detection result from shared detection VM into create/edit form state.
 *
 * The completed result is consumed after copying. This prevents an old detection result
 * from being applied again when the user leaves create/edit without saving and later opens
 * another create/edit screen.
 *
 * @param completedResult Last completed detection result from shared detection VM.
 * @param formState Mutable create/edit form state.
 * @param onCompletedResultConsumed Called after result is copied into the form.
 */
@Composable
internal fun CreateInspectionReportDetectionResultEffect(
  completedResult: DetectCompletedResult?,
  formState: CreateInspectionReportFormViewModel,
  onCompletedResultConsumed: (Long) -> Unit
) {
  LaunchedEffect(completedResult?.refreshId) {
    val result = completedResult ?: return@LaunchedEffect

    formState.applyDetectionResultIfNew(
      refreshId = result.refreshId,
      imagePath = result.imageSource.sourcePath,
      detectResponse = result.detectResponse
    )

    onCompletedResultConsumed(result.refreshId)
  }
}