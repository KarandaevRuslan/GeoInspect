package com.karandaev.geo_inspect.core.ui.components.fields

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow

/**
 * Common text field with optional clear and password visibility actions.
 *
 * @param value Current field value.
 * @param onValueChange Called when value changes.
 * @param label Field label.
 * @param enabled Whether the field is enabled.
 * @param onClear Called when clear button is clicked.
 * @param modifier Modifier applied to the field.
 * @param keyboardOptions Keyboard options used by the field.
 * @param visualTransformation Visual transformation used by the field.
 * @param isPassword Whether the field is a password-like field.
 * @param isPasswordVisible Whether password text is currently visible.
 * @param onPasswordVisibilityToggle Called when password visibility button is clicked.
 * @param isError Whether the field should be shown in error state.
 * @param supportingText Optional supporting text shown below the field.
 * @param labelTextStyle Optional custom label text style.
 */
@Composable
internal fun MyTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  enabled: Boolean,
  onClear: () -> Unit,
  modifier: Modifier = Modifier,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  isPassword: Boolean = false,
  isPasswordVisible: Boolean = false,
  onPasswordVisibilityToggle: (() -> Unit)? = null,
  isError: Boolean = false,
  supportingText: String? = null,
  labelTextStyle: TextStyle? = null
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    enabled = enabled,
    label = {
      if (labelTextStyle == null) {
        Text(
          text = label,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      } else {
        Text(
          text = label,
          style = labelTextStyle,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    },
    trailingIcon = {
      if (value.isNotEmpty()) {
        MyTextFieldTrailingIcon(
          label = label,
          enabled = enabled,
          isPassword = isPassword,
          isPasswordVisible = isPasswordVisible,
          onPasswordVisibilityToggle = onPasswordVisibilityToggle,
          onClear = onClear
        )
      }
    },
    modifier = modifier,
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    singleLine = true,
    isError = isError,
    supportingText = supportingText?.let { text ->
      {
        Text(
          text = text,
          color = if (isError) {
            MaterialTheme.colorScheme.error
          } else {
            MaterialTheme.colorScheme.onSurfaceVariant
          }
        )
      }
    }
  )
}

@Composable
private fun MyTextFieldTrailingIcon(
  label: String,
  enabled: Boolean,
  isPassword: Boolean,
  isPasswordVisible: Boolean,
  onPasswordVisibilityToggle: (() -> Unit)?,
  onClear: () -> Unit
) {
  Row {
    if (isPassword && onPasswordVisibilityToggle != null) {
      IconButton(
        onClick = onPasswordVisibilityToggle,
        enabled = enabled
      ) {
        Icon(
          imageVector = if (isPasswordVisible) {
            Icons.Default.VisibilityOff
          } else {
            Icons.Default.Visibility
          },
          contentDescription = if (isPasswordVisible) {
            "Hide $label"
          } else {
            "Show $label"
          }
        )
      }
    }

    IconButton(
      onClick = onClear,
      enabled = enabled
    ) {
      Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = "Clear $label"
      )
    }
  }
}

/**
 * Returns a password visual transformation based on visibility state.
 */
internal fun passwordVisualTransformation(
  isVisible: Boolean
): VisualTransformation {
  return if (isVisible) {
    VisualTransformation.None
  } else {
    PasswordVisualTransformation()
  }
}