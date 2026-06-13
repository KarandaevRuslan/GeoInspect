package com.karandaev.geo_inspect.core.ui.components.double_picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Displays a reusable double value picker with a text field, slider, and step buttons.
 *
 * The component keeps a local draft value while the user is editing. Text input is committed
 * when the user presses the Done IME action or when the text field loses focus.
 *
 * @param label Label displayed inside the text field.
 * @param value Current value.
 * @param onValueChange Callback invoked when a valid value is committed.
 * @param modifier Modifier applied to the root layout.
 * @param step Increment/decrement step used by repeat buttons.
 * @param min Minimum allowed value.
 * @param max Maximum allowed value.
 * @param enabled Whether the input controls are enabled.
 * @param formatter Formats committed numeric values for display.
 * @param parser Parses text input into a numeric value.
 */
@Composable
fun DoublePicker(
  label: String,
  value: Double,
  onValueChange: (Double) -> Unit,
  modifier: Modifier = Modifier,
  step: Double = DoublePickerDefaults.Step,
  min: Double = DoublePickerDefaults.Min,
  max: Double = DoublePickerDefaults.Max,
  enabled: Boolean = true,
  formatter: (Double) -> String = DoublePickerDefaults::formatValue,
  parser: (String) -> Double? = DoublePickerDefaults::parseValue
) {
  val focusManager = LocalFocusManager.current

  val state = rememberDoublePickerState(
    value = value,
    min = min,
    max = max,
    formatter = formatter,
    parser = parser,
    onValueChange = onValueChange
  )

  LaunchedEffect(value, min, max, state.isTextFieldFocused) {
    state.syncExternalValueIfNeeded(
      value = value,
      min = min,
      max = max
    )
  }

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    OutlinedTextField(
      value = state.text,
      onValueChange = state::updateText,
      enabled = enabled,
      label = { Text(label) },
      textStyle = MaterialTheme.typography.titleMedium,
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(
        onDone = {
          state.commitTextInput()
          focusManager.clearFocus()
        }
      ),
      modifier = Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          state.handleFocusChange(focusState.isFocused)
        }
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      RepeatStepButton(
        text = "−",
        enabled = enabled,
        onStep = {
          state.commitValue(state.draftValue - step)
        }
      )

      Slider(
        value = state.draftValue.toFloat(),
        onValueChange = { newValue ->
          state.updateDraftValue(newValue.toDouble())
        },
        onValueChangeFinished = {
          state.commitValue(state.draftValue)
        },
        valueRange = min.toFloat()..max.toFloat(),
        steps = 0,
        enabled = enabled,
        modifier = Modifier.weight(1f)
      )

      RepeatStepButton(
        text = "+",
        enabled = enabled,
        onStep = {
          state.commitValue(state.draftValue + step)
        }
      )
    }
  }
}