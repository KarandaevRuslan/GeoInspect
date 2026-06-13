package com.karandaev.geo_inspect.feature.presentation.create.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

/**
 * Title input field for the create/edit inspection report form.
 *
 * @param value Current title value.
 * @param onValueChange Called when the title changes.
 * @param modifier Modifier applied to the field container.
 */
@Composable
internal fun CreateInspectionReportTitleField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
  ) {
    Text(
      text = stringResource(id = R.string.create_inspection_report_title_label),
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(bottom = 6.dp)
    )

    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      placeholder = {
        Text(text = stringResource(id = R.string.create_inspection_report_title_placeholder))
      },
      isError = value.isBlank(),
      supportingText = {
        if (value.isBlank()) {
          Text(text = stringResource(id = R.string.create_inspection_report_title_required))
        }
      },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true
    )
  }
}

/**
 * Description input field for the create/edit inspection report form.
 *
 * @param value Current inspection report description value.
 * @param onValueChange Called when the description changes.
 * @param modifier Modifier applied to the field container.
 */
@Composable
internal fun CreateInspectionReportContentField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
  ) {
    Text(
      text = stringResource(id = R.string.create_inspection_report_description_label),
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(bottom = 6.dp)
    )

    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      placeholder = {
        Text(text = stringResource(id = R.string.create_inspection_report_description_placeholder))
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp),
      maxLines = 6
    )
  }
}