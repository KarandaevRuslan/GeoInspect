package com.karandaev.geo_inspect.feature.presentation.settings.components.sections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.feature.presentation.settings.components.items.SettingsActionItem

@Composable
internal fun SettingsDataSection(
  isExporting: Boolean,
  isImporting: Boolean,
  isResetting: Boolean,
  onExportClick: () -> Unit,
  onImportClick: () -> Unit,
  onResetClick: () -> Unit
) {
  SettingsSection(
    title = stringResource(R.string.settings_data_section_title)
  ) {
    SettingsActionItem(
      label = stringResource(R.string.settings_data_import_reports),
      description = stringResource(R.string.settings_data_import_reports_description),
      icon = Icons.Default.Download,
      contentDescription = stringResource(R.string.settings_data_import_reports),
      isLoading = isImporting,
      onClick = onImportClick
    )

    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

    SettingsActionItem(
      label = stringResource(R.string.settings_data_export_reports),
      description = stringResource(R.string.settings_data_export_reports_description),
      icon = Icons.Default.Upload,
      contentDescription = stringResource(R.string.settings_data_export_reports),
      isLoading = isExporting,
      onClick = onExportClick
    )

    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

    SettingsActionItem(
      label = stringResource(R.string.settings_data_reset_reports),
      description = stringResource(R.string.settings_data_reset_reports_description),
      icon = Icons.Default.Restore,
      contentDescription = stringResource(R.string.settings_data_reset_reports),
      isLoading = isResetting,
      onClick = onResetClick
    )
  }
}