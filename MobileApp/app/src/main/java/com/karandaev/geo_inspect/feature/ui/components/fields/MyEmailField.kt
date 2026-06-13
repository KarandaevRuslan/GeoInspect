package com.karandaev.geo_inspect.feature.ui.components.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.karandaev.geo_inspect.core.ui.components.fields.MyTextField

/**
 * Email input field used by auth screens.
 *
 * @param email Current email value.
 * @param enabled Whether the field is enabled.
 * @param onEmailChange Called when email changes.
 * @param onEmailClear Called when clear button is clicked.
 * @param modifier Modifier applied to the field.
 */
@Composable
internal fun MyEmailField(
  email: String,
  enabled: Boolean,
  onEmailChange: (String) -> Unit,
  onEmailClear: () -> Unit,
  modifier: Modifier = Modifier
) {
  MyTextField(
    value = email,
    onValueChange = onEmailChange,
    enabled = enabled,
    label = "Email",
    onClear = onEmailClear,
    modifier = modifier,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Email
    )
  )
}