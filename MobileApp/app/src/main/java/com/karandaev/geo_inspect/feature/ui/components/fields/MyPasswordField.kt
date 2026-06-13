package com.karandaev.geo_inspect.feature.ui.components.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.karandaev.geo_inspect.core.ui.components.fields.MyTextField
import com.karandaev.geo_inspect.core.ui.components.fields.passwordVisualTransformation

/**
 * Password input field used by email/password auth screens.
 *
 * @param password Current password value.
 * @param enabled Whether the field is enabled.
 * @param onPasswordChange Called when password changes.
 * @param onPasswordClear Called when clear button is clicked.
 * @param modifier Modifier applied to the field.
 * @param label Field label.
 * @param labelTextStyle Optional custom label text style.
 */
@Composable
internal fun MyPasswordField(
  password: String,
  enabled: Boolean,
  onPasswordChange: (String) -> Unit,
  onPasswordClear: () -> Unit,
  modifier: Modifier = Modifier,
  label: String = "Password",
  labelTextStyle: TextStyle? = null
) {
  var isPasswordVisible by remember {
    mutableStateOf(false)
  }

  MyTextField(
    value = password,
    onValueChange = onPasswordChange,
    enabled = enabled,
    label = label,
    onClear = onPasswordClear,
    modifier = modifier,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Password
    ),
    visualTransformation = passwordVisualTransformation(
      isVisible = isPasswordVisible
    ),
    isPassword = true,
    isPasswordVisible = isPasswordVisible,
    onPasswordVisibilityToggle = {
      isPasswordVisible = !isPasswordVisible
    },
    labelTextStyle = labelTextStyle
  )
}