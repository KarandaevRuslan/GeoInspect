package com.karandaev.geo_inspect.feature.ui.components.cards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.ui.components.card.AppCard
import com.karandaev.geo_inspect.core.ui.components.card.AppCardContent
import com.karandaev.geo_inspect.core.ui.components.card.AppCardDefaults
import com.karandaev.geo_inspect.core.ui.components.card.AppCardIconBox
import com.karandaev.geo_inspect.core.ui.components.card.AppCardSwipeAction
import com.karandaev.geo_inspect.core.ui.components.card.AppSwipeActionCard
import com.karandaev.geo_inspect.core.util.formatters.formatLastUpdatedAt

private val ReportCardIconSize = 40.dp
private val ReportCardIconPadding = 10.dp
private val ReportCardEditButtonSize = 40.dp
private val ReportCardEditIconSize = 20.dp

/**
 * Report card with feature-specific swipe actions.
 *
 * Swipe behavior:
 * - start-to-end swipe exports the report;
 * - end-to-start swipe deletes the report.
 *
 * The base [AppCard] remains generic. Report-specific behavior is composed here.
 *
 * @param inspectionReport Report displayed by the card.
 * @param onClick Called when the card is clicked.
 * @param onEdit Called when edit action is clicked.
 * @param onExport Called when the report export swipe action is triggered.
 * @param onDelete Called when the report delete swipe action is triggered.
 * @param modifier Modifier applied to the swipeable card.
 */
@Composable
fun ReportCard(
  inspectionReport: InspectionReport,
  onClick: () -> Unit,
  onEdit: () -> Unit,
  onExport: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiModel = remember(inspectionReport) {
    ReportCardUiModel(
      title = inspectionReport.title,
      subtitle = inspectionReport.content.asSingleLinePreview(),
      supportingText = formatLastUpdatedAt(inspectionReport.lastUpdatedAtMillis)
    )
  }

  val cardShape = AppCardDefaults.shape

  val exportAction = AppCardSwipeAction(
    label = "Export",
    icon = Icons.Default.FileDownload,
    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
  )

  val deleteAction = AppCardSwipeAction(
    label = "Delete",
    icon = Icons.Default.Delete,
    backgroundColor = MaterialTheme.colorScheme.errorContainer,
    contentColor = MaterialTheme.colorScheme.onErrorContainer
  )

  AppSwipeActionCard(
    modifier = modifier.fillMaxWidth(),
    shape = cardShape,
    startToEndAction = exportAction,
    endToStartAction = deleteAction,
    onStartToEnd = onExport,
    onEndToStart = onDelete
  ) {
    AppCard(
      modifier = Modifier.fillMaxWidth(),
      shape = cardShape,
      onClick = onClick
    ) {
      AppCardContent(
        title = uiModel.title,
        subtitle = uiModel.subtitle,
        supportingText = uiModel.supportingText,
        verticalAlignment = Alignment.CenterVertically,
        leadingContent = {
          AppCardIconBox {
            Icon(
              imageVector = Icons.Default.Description,
              contentDescription = null,
              modifier = Modifier
                .size(ReportCardIconSize)
                .padding(ReportCardIconPadding)
            )
          }
        },
        trailingContent = {
          IconButton(
            onClick = onEdit,
            modifier = Modifier.size(ReportCardEditButtonSize)
          ) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "Edit report",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(ReportCardEditIconSize)
            )
          }
        }
      )
    }
  }
}

private data class ReportCardUiModel(
  val title: String,
  val subtitle: String,
  val supportingText: String
)

/**
 * Converts multiline report content into a compact single-line preview.
 *
 * @return Content with line breaks replaced by spaces.
 */
private fun String.asSingleLinePreview(): String {
  return replace("\n", " ")
}