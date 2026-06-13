package com.karandaev.geo_inspect.feature.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.karandaev.geo_inspect.BuildConfig
import com.karandaev.geo_inspect.R
import com.karandaev.geo_inspect.app.rememberMyApp
import com.karandaev.geo_inspect.core.domain.model.InspectionReport
import com.karandaev.geo_inspect.core.json.export_json.rememberDirectoryExporter
import com.karandaev.geo_inspect.core.json.export_json.reports.exportInspectionReportsDirectory
import com.karandaev.geo_inspect.core.json.import_json.rememberDirectoryImporter
import com.karandaev.geo_inspect.core.json.import_json.reports.importInspectionReportsDirectory
import com.karandaev.geo_inspect.core.presentation.auth.AuthViewModel
import com.karandaev.geo_inspect.core.presentation.auth.model.currentProfile
import com.karandaev.geo_inspect.core.presentation.settings.RESET_DATA_CONFIRMATION_WORD
import com.karandaev.geo_inspect.core.presentation.settings.ServerAvailabilityState
import com.karandaev.geo_inspect.core.presentation.settings.SettingsDialogState
import com.karandaev.geo_inspect.core.presentation.settings.SettingsViewModel
import com.karandaev.geo_inspect.feature.presentation.settings.dialogs.SettingsResetDataDialogHost
import com.karandaev.geo_inspect.feature.presentation.settings.dialogs.SettingsServerBaseUrlDialogHost
import com.karandaev.geo_inspect.feature.presentation.settings.reset.rememberResetNotesToDefaultsAction

@Composable
fun SettingsRoute(
  settingsViewModel: SettingsViewModel,
  authViewModel: AuthViewModel,
  inspectionReports: List<InspectionReport>,
  onProfileClick: () -> Unit,
  onSignInClick: () -> Unit
) {
  val app = rememberMyApp()

  val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
  val settingsRuntimeUiState by settingsViewModel.runtimeUiState.collectAsStateWithLifecycle()
  val authState by authViewModel.state.collectAsStateWithLifecycle()

  var isExporting by remember { mutableStateOf(false) }
  var isImporting by remember { mutableStateOf(false) }
  var isResetting by remember { mutableStateOf(false) }

  val exportReportsSuccessMessage = stringResource(
    R.string.settings_data_export_reports_success
  )
  val importReportsSuccessMessage = stringResource(
    R.string.settings_data_import_reports_success
  )

  LaunchedEffect(Unit) {
    settingsViewModel.checkSavedServerBaseUrlIfStale()
  }

  LaunchedEffect(
    authState.currentProfile?.uid,
    settingsRuntimeUiState.savedServerAvailabilityState
  ) {
    if (settingsRuntimeUiState.savedServerAvailabilityState == ServerAvailabilityState.Available) {
      authViewModel.refreshCurrentUserAvatarUrlIfStale()
    }
  }

  val resetDataDialogState = settingsRuntimeUiState.dialogState as? SettingsDialogState.ResetData
  val serverBaseUrlDialogState =
    settingsRuntimeUiState.dialogState as? SettingsDialogState.ChangeServerBaseUrl

  val exportInspectionReports = rememberDirectoryExporter<List<InspectionReport>>(
    exportToDirectory = { context, directoryUri, reportsToExport ->
      exportInspectionReportsDirectory(
        context = context,
        parentDirectoryUri = directoryUri,
        inspectionReports = reportsToExport,
        moshi = app.moshi
      )
    },
    successMessage = exportReportsSuccessMessage,
    onExportingChange = { exporting ->
      isExporting = exporting
    }
  )

  val importInspectionReports = rememberDirectoryImporter(
    importFromDirectory = { context, directoryUri ->
      importInspectionReportsDirectory(
        context = context,
        directoryUri = directoryUri,
        moshi = app.moshi
      )
    },
    onImport = { context, importPayload ->
      val insertedInspectionReportIds = settingsViewModel.importInspectionReports(
        inspectionReports = importPayload.inspectionReports
      )

      importPayload.saveImagesForInsertedReports(
        context = context,
        insertedInspectionReportIds = insertedInspectionReportIds
      )
    },
    successMessage = importReportsSuccessMessage,
    onImportingChange = { importing ->
      isImporting = importing
    }
  )

  val resetNotesToDefaults = rememberResetNotesToDefaultsAction(
    resetNotesToDefaults = settingsViewModel::resetInspectionReportsToDefaults,
    onResettingChange = { resetting ->
      isResetting = resetting
    }
  )

  SettingsResetDataDialogHost(
    showDialog = resetDataDialogState != null,
    confirmationText = resetDataDialogState?.confirmationText.orEmpty(),
    requiredConfirmationWord = RESET_DATA_CONFIRMATION_WORD,
    isResetting = isResetting,
    onConfirmationTextChange = settingsViewModel::setResetDataConfirmationText,
    onConfirmationTextClear = settingsViewModel::clearResetDataConfirmationText,
    onDismiss = settingsViewModel::clearDialogState,
    onConfirm = resetNotesToDefaults
  )

  SettingsServerBaseUrlDialogHost(
    showDialog = serverBaseUrlDialogState != null,
    serverBaseUrl = serverBaseUrlDialogState?.draftServerBaseUrl.orEmpty(),
    availabilityState = serverBaseUrlDialogState?.draftServerAvailabilityState
      ?: settingsRuntimeUiState.savedServerAvailabilityState,
    enabled = !isExporting && !isImporting && !isResetting,
    onServerBaseUrlChange = settingsViewModel::setDraftServerBaseUrl,
    onServerBaseUrlClear = settingsViewModel::clearDraftServerBaseUrl,
    onCheckServerClick = settingsViewModel::checkDraftServerBaseUrl,
    onDismiss = settingsViewModel::clearDialogState,
    onSave = settingsViewModel::saveDraftServerBaseUrl
  )

  SettingsScreen(
    profile = authState.currentProfile,
    themeMode = settings.themeMode,
    serverBaseUrl = settings.serverBaseUrl,
    serverAvailabilityState = settingsRuntimeUiState.savedServerAvailabilityState,
    appVersionName = BuildConfig.VERSION_NAME,
    isExporting = isExporting,
    isImporting = isImporting,
    isResetting = isResetting,
    onProfileClick = onProfileClick,
    onSignInClick = onSignInClick,
    onThemeModeSelect = settingsViewModel::setThemeMode,
    onServerBaseUrlClick = settingsViewModel::showChangeServerBaseUrlDialog,
    onCheckServerClick = settingsViewModel::checkSavedServerBaseUrl,
    onExportClick = {
      exportInspectionReports(inspectionReports)
    },
    onImportClick = importInspectionReports,
    onResetClick = settingsViewModel::showResetDataDialog
  )
}