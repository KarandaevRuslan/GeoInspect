package com.karandaev.geo_inspect.core.ui.components.double_picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue

/**
 * Holds editable state for [DoublePicker].
 *
 * This state object separates text input, parsing, value clamping, focus handling, and commit
 * logic from the composable UI tree.
 */
class DoublePickerState internal constructor(
  initialValue: Double,
  private var min: Double,
  private var max: Double,
  private val formatter: (Double) -> String,
  private val parser: (String) -> Double?,
  private val onValueChange: (Double) -> Unit
) {
  var draftValue by mutableDoubleStateOf(initialValue.coerceIn(min, max))
    private set

  var text by mutableStateOf(formatter(initialValue.coerceIn(min, max)))
    private set

  var isTextFieldFocused by mutableStateOf(false)
    private set

  /**
   * Synchronizes the local draft state with an external value when the text field is not focused.
   *
   * This prevents external updates from overwriting text currently being edited by the user.
   */
  fun syncExternalValueIfNeeded(
    value: Double,
    min: Double,
    max: Double
  ) {
    this.min = min
    this.max = max

    if (!isTextFieldFocused) {
      val clamped = value.coerceIn(min, max)
      draftValue = clamped
      text = formatter(clamped)
    }
  }

  /**
   * Updates the raw text field value without formatting it.
   *
   * The value is intentionally kept as typed to avoid cursor jumps and visual flickering while
   * the user edits the text.
   */
  fun updateText(input: String) {
    text = input
  }

  /**
   * Updates the draft value without notifying the parent.
   *
   * This is useful for slider dragging where the value should be committed only after interaction
   * finishes.
   */
  fun updateDraftValue(value: Double) {
    val clamped = value.coerceIn(min, max)
    draftValue = clamped
    text = formatter(clamped)
  }

  /**
   * Commits a numeric value to the parent callback after clamping it to the allowed range.
   */
  fun commitValue(value: Double) {
    val clamped = value.coerceIn(min, max)
    draftValue = clamped
    text = formatter(clamped)
    onValueChange(clamped)
  }

  /**
   * Parses and commits the current text field value.
   *
   * If parsing fails, the text is reverted to the latest valid draft value.
   */
  fun commitTextInput() {
    val parsed = parser(text)

    if (parsed != null) {
      commitValue(parsed)
    } else {
      text = formatter(draftValue)
    }
  }

  /**
   * Handles focus transitions and commits text input when focus leaves the text field.
   */
  fun handleFocusChange(isFocused: Boolean) {
    if (isTextFieldFocused && !isFocused) {
      commitTextInput()
    }

    isTextFieldFocused = isFocused
  }
}

/**
 * Creates and remembers a [DoublePickerState] instance.
 */
@Composable
fun rememberDoublePickerState(
  value: Double,
  min: Double,
  max: Double,
  formatter: (Double) -> String,
  parser: (String) -> Double?,
  onValueChange: (Double) -> Unit
): DoublePickerState {
  val currentFormatter by rememberUpdatedState(formatter)
  val currentParser by rememberUpdatedState(parser)
  val currentOnValueChange by rememberUpdatedState(onValueChange)

  return remember {
    DoublePickerState(
      initialValue = value,
      min = min,
      max = max,
      formatter = { currentFormatter(it) },
      parser = { currentParser(it) },
      onValueChange = { currentOnValueChange(it) }
    )
  }
}