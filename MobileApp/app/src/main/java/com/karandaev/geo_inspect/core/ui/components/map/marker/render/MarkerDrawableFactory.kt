package com.karandaev.geo_inspect.core.ui.components.map.marker.render

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

private const val MARKER_STROKE_WIDTH_DP = 2

/**
 * Creates a circular marker drawable with the requested visual style.
 */
internal fun circleMarkerDrawable(
  context: Context,
  fillColor: Int,
  strokeColor: Int,
  sizeDp: Int
): Drawable {
  val density = context.resources.displayMetrics.density
  val sizePx = (sizeDp * density).toInt()
  val strokePx = (MARKER_STROKE_WIDTH_DP * density).toInt()

  return GradientDrawable().apply {
    shape = GradientDrawable.OVAL
    setColor(fillColor)
    setStroke(strokePx, strokeColor)
    setSize(sizePx, sizePx)
  }
}