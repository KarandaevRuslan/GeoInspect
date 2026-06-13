package com.karandaev.geo_inspect.feature.ui.components.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.karandaev.geo_inspect.R

/**
 * Reset confirmation input field.
 *
 * The field is highlighted as error when it is not empty and does not match
 * the required confirmation word.
 *
 * @param confirmationText Current confirmation text.
 * @param requiredConfirmationWord Word required to confirm reset.
 * @param enabled Whether the field is enabled.
 * @param onConfirmationTextChange Called when confirmation text changes.
 * @param onConfirmationTextClear Called when clear button is clicked.
 * @param modifier Modifier applied to the field.
 */
@Composable
internal fun MyResetConfirmationField(
  confirmationText: String,
  requiredConfirmationWord: String,
  enabled: Boolean,
  onConfirmationTextChange: (String) -> Unit,
  onConfirmationTextClear: () -> Unit,
  modifier: Modifier = Modifier
) {
  MyConfirmationWordField(
    confirmationText = confirmationText,
    requiredConfirmationWord = requiredConfirmationWord,
    label = stringResource(R.string.confirmation_word_field_label),
    promptText = stringResource(
      id = R.string.reset_confirmation_word_field_prompt,
      requiredConfirmationWord
    ),
    enabled = enabled,
    onConfirmationTextChange = onConfirmationTextChange,
    onConfirmationTextClear = onConfirmationTextClear,
    modifier = modifier
  )
}