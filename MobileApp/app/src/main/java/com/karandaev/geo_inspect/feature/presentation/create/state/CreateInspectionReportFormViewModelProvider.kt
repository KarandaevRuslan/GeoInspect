package com.karandaev.geo_inspect.feature.presentation.create.state

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karandaev.geo_inspect.core.domain.model.location.MapPoint
import com.karandaev.geo_inspect.core.presentation.savedStateViewModelFactory

/**
 * Creates create/edit note form state scoped to the current navigation destination.
 *
 * This avoids separate Compose saveable states for portrait and landscape layouts.
 */
@Composable
internal fun rememberCreateInspectionReportFormViewModel(
  initialSelectedPoint: MapPoint?,
  viewModelKey: String
): CreateInspectionReportFormViewModel {
  return viewModel(
    key = viewModelKey,
    factory = savedStateViewModelFactory { savedStateHandle ->
      CreateInspectionReportFormViewModel(
        savedStateHandle = savedStateHandle,
        initialSelectedPoint = initialSelectedPoint
      )
    }
  )
}