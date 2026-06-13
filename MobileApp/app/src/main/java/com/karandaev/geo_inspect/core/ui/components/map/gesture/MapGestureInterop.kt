package com.karandaev.geo_inspect.core.ui.components.map.gesture

import android.annotation.SuppressLint
import android.view.MotionEvent
import org.osmdroid.views.MapView

/**
 * Prevents parent scroll containers from intercepting active map gestures.
 */
@SuppressLint("ClickableViewAccessibility")
internal fun MapView.disallowParentInterceptDuringMapGestures() {
  setOnTouchListener { view, event ->
    val disallowParentTouch = when (event.actionMasked) {
      MotionEvent.ACTION_DOWN,
      MotionEvent.ACTION_MOVE,
      MotionEvent.ACTION_POINTER_DOWN,
      MotionEvent.ACTION_POINTER_UP -> true

      MotionEvent.ACTION_UP,
      MotionEvent.ACTION_CANCEL -> false

      else -> true
    }

    view.parent?.requestDisallowInterceptTouchEvent(disallowParentTouch)

    false
  }
}