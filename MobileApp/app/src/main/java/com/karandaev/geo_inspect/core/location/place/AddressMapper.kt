package com.karandaev.geo_inspect.core.location.place

import android.location.Address

/**
 * Converts Android [Address] values into user-facing place names.
 */
internal fun Address?.toPlaceName(): String {
  return this?.locality
    ?: this?.subAdminArea
    ?: this?.adminArea
    ?: this?.countryName
    ?: ""
}