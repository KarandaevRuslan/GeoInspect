package com.karandaev.geo_inspect.feature.presentation.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.karandaev.geo_inspect.core.presentation.auth.AuthViewModel
import com.karandaev.geo_inspect.core.presentation.auth.DELETE_ACCOUNT_CONFIRMATION_WORD
import com.karandaev.geo_inspect.core.presentation.auth.model.AuthDialogState
import com.karandaev.geo_inspect.core.presentation.auth.model.currentProfile
import com.karandaev.geo_inspect.core.presentation.launcher.rememberImagePickerLauncher
import com.karandaev.geo_inspect.feature.presentation.profile.dialogs.ProfileAvatarDialogHost
import com.karandaev.geo_inspect.feature.presentation.profile.dialogs.ProfileChangePasswordDialogHost
import com.karandaev.geo_inspect.feature.presentation.profile.dialogs.ProfileChangeUserNameDialogHost
import com.karandaev.geo_inspect.feature.presentation.profile.dialogs.ProfileDeleteAccountDialogHost
import com.karandaev.geo_inspect.feature.presentation.profile.dialogs.ProfileReauthenticationDialogHost

/**
 * Logical wrapper for the profile screen.
 *
 * This route observes auth state, hosts profile dialogs, and connects UI events to [AuthViewModel].
 *
 * Reauthentication dialog has priority over regular profile dialogs. This prevents showing
 * two modal dialogs at the same time during sensitive operations.
 *
 * @param authViewModel Authentication ViewModel.
 * @param onBackClick Called when the user clicks the back button.
 */
@Composable
fun ProfileRoute(
  authViewModel: AuthViewModel,
  onBackClick: () -> Unit
) {
  val context = LocalContext.current
  val state by authViewModel.state.collectAsStateWithLifecycle()

  val profile = state.currentProfile
  val dialogState = state.dialogState
  val isReauthenticationRequired = state.isReauthenticationRequired
  val isRegularDialogVisible = !isReauthenticationRequired

  val avatarPicker = rememberImagePickerLauncher(
    onImageSelected = authViewModel::setProfileAvatarImage
  )

  ProfileChangeUserNameDialogHost(
    showDialog = isRegularDialogVisible &&
      dialogState is AuthDialogState.ChangeUserName,
    userName = state.userName,
    message = state.message,
    enabled = !state.isLoading,
    onUserNameChange = authViewModel::setUserName,
    onUserNameClear = authViewModel::clearUserName,
    onClearMessageClick = authViewModel::clearMessage,
    onDismiss = authViewModel::dismissChangeUserNameDialog,
    onSave = authViewModel::updateCurrentUserName
  )

  ProfileChangePasswordDialogHost(
    showDialog = isRegularDialogVisible &&
      dialogState is AuthDialogState.ChangePassword,
    password = state.password,
    repeatedPassword = state.repeatedPassword,
    passwordStrength = state.passwordStrength,
    message = state.message,
    enabled = !state.isLoading,
    onPasswordChange = authViewModel::setPassword,
    onPasswordClear = authViewModel::clearPassword,
    onRepeatedPasswordChange = authViewModel::setRepeatedPassword,
    onRepeatedPasswordClear = authViewModel::clearRepeatedPassword,
    onClearMessageClick = authViewModel::clearMessage,
    onDismiss = authViewModel::dismissChangePasswordDialog,
    onSave = authViewModel::changePassword
  )

  ProfileAvatarDialogHost(
    showDialog = isRegularDialogVisible &&
      dialogState is AuthDialogState.ChangeAvatar,
    profile = profile,
    avatarImageSource = state.avatarImageSource,
    message = state.message,
    enabled = !state.isLoading,
    onChooseImageClick = avatarPicker::launchImagePicker,
    onCropBoxChange = authViewModel::setSilentlyProfileAvatarCropBox,
    onClearSelectedImageClick = authViewModel::clearProfileAvatarImage,
    onClearMessageClick = authViewModel::clearMessage,
    onDismiss = authViewModel::dismissChangeAvatarDialog,
    onUpdateAvatarClick = {
      authViewModel.updateCurrentUserAvatar(context)
    },
    onResetAvatarClick = authViewModel::resetCurrentUserAvatar
  )

  ProfileDeleteAccountDialogHost(
    showDialog = isRegularDialogVisible &&
      dialogState is AuthDialogState.DeleteAccount,
    confirmationText = state.deleteAccountConfirmationText,
    requiredConfirmationWord = DELETE_ACCOUNT_CONFIRMATION_WORD,
    message = state.message,
    isLoading = state.isLoading,
    onConfirmationTextChange = authViewModel::setDeleteAccountConfirmationText,
    onConfirmationTextClear = authViewModel::clearDeleteAccountConfirmationText,
    onClearMessageClick = authViewModel::clearMessage,
    onDismiss = authViewModel::dismissDeleteAccountDialog,
    onConfirm = authViewModel::deleteAccount
  )

  ProfileReauthenticationDialogHost(
    showDialog = isReauthenticationRequired,
    originalPassword = state.originalPassword,
    message = state.message,
    enabled = !state.isLoading,
    onOriginalPasswordChange = authViewModel::setOriginalPassword,
    onOriginalPasswordClear = authViewModel::clearOriginalPassword,
    onClearMessageClick = authViewModel::clearMessage,
    onDismiss = authViewModel::cancelSensitiveOperation,
    onConfirm = authViewModel::reauthenticateCurrentUser
  )

  ProfileScreen(
    profile = profile,
    isLoading = state.isLoading,
    onBackClick = onBackClick,
    onAvatarClick = authViewModel::showChangeAvatarDialog,
    onDisplayNameClick = {
      authViewModel.setUserName(profile?.displayName.orEmpty())
      authViewModel.showChangeUserNameDialog()
    },
    onChangePasswordClick = authViewModel::showChangePasswordDialog,
    onDeleteAccountClick = authViewModel::showDeleteAccountDialog,
    onLogoutClick = authViewModel::logout
  )
}