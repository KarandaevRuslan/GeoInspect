package com.karandaev.geo_inspect.core.util.other

import android.content.Context
import android.widget.Toast

/**
 * Shows a short toast message.
 *
 * @param message Message that should be displayed to the user.
 */
internal fun Context.toast(
  message: String
) {
  Toast.makeText(
    this,
    message,
    Toast.LENGTH_SHORT
  ).show()
}