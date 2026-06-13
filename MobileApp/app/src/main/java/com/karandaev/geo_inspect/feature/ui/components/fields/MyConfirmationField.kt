package com.karandaev.geo_inspect.feature.ui.components.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.karandaev.geo_inspect.core.ui.components.fields.MyTextField

/**
 * Confirmation input field that requires exact confirmation word.
 *
 * @param confirmationText Current confirmation text.
 * @param requiredConfirmationWord Word required to confirm the action.
 * @param label Field label.
 * @param promptText Supporting prompt text.
 * @param enabled Whether the field is enabled.
 * @param onConfirmationTextChange Called when confirmation text changes.
 * @param onConfirmationTextClear Called when clear button is clicked.
 * @param modifier Modifier applied to the field.
 */
@Composable
internal fun MyConfirmationWordField(
  confirmationText: String,
  requiredConfirmationWord: String,
  label: String,
  promptText: String,
  enabled: Boolean,
  onConfirmationTextChange: (String) -> Unit,
  onConfirmationTextClear: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isInvalid = confirmationText.isNotEmpty() &&
    confirmationText.trim() != requiredConfirmationWord

  MyTextField(
    value = confirmationText,
    onValueChange = onConfirmationTextChange,
    enabled = enabled,
    label = label,
    onClear = onConfirmationTextClear,
    modifier = modifier,
    keyboardOptions = KeyboardOptions(
      capitalization = KeyboardCapitalization.Characters,
      keyboardType = KeyboardType.Text
    ),
    isError = isInvalid,
    supportingText = promptText
  )
}