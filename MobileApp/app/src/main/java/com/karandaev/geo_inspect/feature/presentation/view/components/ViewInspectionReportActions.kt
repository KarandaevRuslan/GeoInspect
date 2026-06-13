package com.karandaev.geo_inspect.feature.presentation.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val ViewInspectionReportActionsTopPadding = 14.dp
private val ViewInspectionReportActionsSpacing = 12.dp
private val ViewInspectionReportActionButtonHeight = 46.dp
private val ViewInspectionReportActionButtonShape = RoundedCornerShape(18.dp)
private val ViewInspectionReportActionIconSize = 18.dp

/**
 * Report details action buttons.
 */
@Composable
internal fun ViewInspectionReportActions(
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(
        top = ViewInspectionReportActionsTopPadding
      ),
    horizontalArrangement = Arrangement.spacedBy(ViewInspectionReportActionsSpacing)
  ) {
    Button(
      onClick = onEditClick,
      modifier = Modifier
        .weight(1f)
        .height(ViewInspectionReportActionButtonHeight),
      shape = ViewInspectionReportActionButtonShape,
      contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
      Icon(
        imageVector = Icons.Default.Edit,
        contentDescription = null,
        modifier = Modifier.size(ViewInspectionReportActionIconSize)
      )

      Text(
        text = "Edit",
        modifier = Modifier.padding(start = 8.dp)
      )
    }

    OutlinedButton(
      onClick = onDeleteClick,
      modifier = Modifier
        .weight(1f)
        .height(ViewInspectionReportActionButtonHeight),
      shape = ViewInspectionReportActionButtonShape,
      border = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
      ),
      colors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.error
      ),
      contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
      Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = null,
        modifier = Modifier.size(ViewInspectionReportActionIconSize)
      )

      Text(
        text = "Delete",
        modifier = Modifier.padding(start = 8.dp)
      )
    }
  }
}