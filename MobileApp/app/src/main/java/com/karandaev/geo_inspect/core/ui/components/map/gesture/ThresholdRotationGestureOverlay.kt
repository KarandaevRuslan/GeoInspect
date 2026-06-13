package com.karandaev.geo_inspect.core.ui.components.map.gesture

import android.view.MotionEvent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import kotlin.math.abs
import kotlin.math.atan2

private const val ROTATION_START_THRESHOLD_DEGREES = 3f
private const val ORIENTATION_SMOOTHING = 0.85f

internal class ThresholdRotationGestureOverlay : Overlay() {

  private var firstPointerId = MotionEvent.INVALID_POINTER_ID
  private var secondPointerId = MotionEvent.INVALID_POINTER_ID

  private var startAngle = 0f
  private var startMapOrientation = 0f
  private var lastAppliedOrientation = 0f

  private var isRotationActive = false

  override fun onTouchEvent(
    event: MotionEvent,
    mapView: MapView
  ): Boolean {
    if (!isEnabled) {
      resetGesture()
      return false
    }

    when (event.actionMasked) {
      MotionEvent.ACTION_POINTER_DOWN -> {
        if (event.pointerCount != 2) {
          return false
        }

        firstPointerId = event.getPointerId(0)
        secondPointerId = event.getPointerId(1)

        startAngle = event.rotationAngleOrNull() ?: return false
        startMapOrientation = mapView.mapOrientation
        lastAppliedOrientation = startMapOrientation

        isRotationActive = false

        return false
      }

      MotionEvent.ACTION_MOVE -> {
        if (event.pointerCount != 2) {
          resetGesture()
          return false
        }

        val currentAngle = event.rotationAngleOrNull() ?: return false
        val totalRotation = normalizeAngle(currentAngle - startAngle)

        if (!isRotationActive) {
          if (abs(totalRotation) < ROTATION_START_THRESHOLD_DEGREES) {
            return false
          }

          isRotationActive = true
        }

        val targetOrientation = normalizeOrientation(
          startMapOrientation + totalRotation
        )

        val smoothedOrientation = lastAppliedOrientation +
          normalizeAngle(targetOrientation - lastAppliedOrientation) * ORIENTATION_SMOOTHING

        lastAppliedOrientation = normalizeOrientation(smoothedOrientation)

        mapView.setMapOrientation(lastAppliedOrientation)

        return false
      }

      MotionEvent.ACTION_POINTER_UP,
      MotionEvent.ACTION_UP,
      MotionEvent.ACTION_CANCEL -> {
        resetGesture()
        return false
      }
    }

    return false
  }

  internal fun resetGesture() {
    firstPointerId = MotionEvent.INVALID_POINTER_ID
    secondPointerId = MotionEvent.INVALID_POINTER_ID

    startAngle = 0f
    startMapOrientation = 0f
    lastAppliedOrientation = 0f

    isRotationActive = false
  }

  private fun MotionEvent.rotationAngleOrNull(): Float? {
    val firstPointerIndex = findPointerIndex(firstPointerId)
    val secondPointerIndex = findPointerIndex(secondPointerId)

    if (firstPointerIndex == -1 || secondPointerIndex == -1) {
      return null
    }

    val deltaX = getX(firstPointerIndex) - getX(secondPointerIndex)
    val deltaY = getY(firstPointerIndex) - getY(secondPointerIndex)

    return Math.toDegrees(
      atan2(deltaY.toDouble(), deltaX.toDouble())
    ).toFloat()
  }
}

private fun normalizeAngle(angle: Float): Float {
  return when {
    angle > 180f -> angle - 360f
    angle < -180f -> angle + 360f
    else -> angle
  }
}

private fun normalizeOrientation(orientation: Float): Float {
  return when {
    orientation > 180f -> orientation - 360f
    orientation < -180f -> orientation + 360f
    else -> orientation
  }
}