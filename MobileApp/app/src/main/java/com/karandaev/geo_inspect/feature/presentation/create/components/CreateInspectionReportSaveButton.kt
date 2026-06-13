package com.karandaev.geo_inspect.feature.presentation.create.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karandaev.geo_inspect.R

/**
 * Save button used by the create/edit inspection report screen.
 *
 * @param enabled Whether the button is enabled.
 * @param onClick Called when the user clicks the button.
 * @param modifier Modifier applied to the button.
 */
@Composable
internal fun CreateInspectionReportSaveButton(
  enabled: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Button(
    enabled = enabled,
    onClick = onClick,
    modifier = modifier
      .fillMaxWidth()
      .height(52.dp),
    shape = RoundedCornerShape(28.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.primary
    )
  ) {
    Text(
      text = stringResource(id = R.string.create_inspection_report_save_button),
      style = MaterialTheme.typography.labelLarge
    )
  }
}