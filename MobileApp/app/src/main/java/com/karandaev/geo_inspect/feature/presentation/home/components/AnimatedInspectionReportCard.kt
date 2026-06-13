package com.karandaev.geo_inspect.feature.presentation.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.feature.presentation.home.NOTE_DELETE_ANIMATION_DURATION_MILLIS
import com.karandaev.geo_inspect.feature.ui.components.cards.ReportCard
import kotlinx.coroutines.delay

/**
 * Report card wrapper with delayed delete animation.
 *
 * The card is not deleted immediately after the delete action. Instead, it first
 * plays the exit animation and then calls [onDeleteAnimationFinished].
 *
 * @param inspectionReport Report displayed by the card.
 * @param isDeleting Whether this report is currently playing delete animation.
 * @param onClick Called when the report card is clicked.
 * @param onEdit Called when the report edit action is clicked.
 * @param onExport Called when the report export swipe action is triggered.
 * @param onDeleteRequest Called when the report delete swipe action is triggered.
 * @param onDeleteAnimationFinished Called after the delete animation finishes.
 * @param modifier Modifier applied to the visibility wrapper.
 */
@Composable
internal fun AnimatedInspectionReportCard(
  inspectionReport: InspectionReport,
  isDeleting: Boolean,
  onClick: () -> Unit,
  onEdit: () -> Unit,
  onExport: () -> Unit,
  onDeleteRequest: () -> Unit,
  onDeleteAnimationFinished: () -> Unit,
  modifier: Modifier = Modifier
) {
  LaunchedEffect(inspectionReport.id, isDeleting) {
    if (isDeleting) {
      delay(NOTE_DELETE_ANIMATION_DURATION_MILLIS.toLong())
      onDeleteAnimationFinished()
    }
  }

  AnimatedVisibility(
    visible = !isDeleting,
    modifier = modifier,
    enter = fadeIn(
      animationSpec = tween(NOTE_DELETE_ANIMATION_DURATION_MILLIS)
    ) + expandVertically(
      animationSpec = tween(NOTE_DELETE_ANIMATION_DURATION_MILLIS)
    ),
    exit = fadeOut(
      animationSpec = tween(NOTE_DELETE_ANIMATION_DURATION_MILLIS)
    ) + shrinkVertically(
      animationSpec = tween(NOTE_DELETE_ANIMATION_DURATION_MILLIS)
    )
  ) {
    ReportCard(
      inspectionReport = inspectionReport,
      onClick = onClick,
      onEdit = onEdit,
      onExport = onExport,
      onDelete = onDeleteRequest
    )
  }
}