package com.karandaev.geo_inspect.core.auth.firebase

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser

internal fun FirebaseUser.hasEmailPasswordProvider(): Boolean {
  return providerData.any { userInfo ->
    userInfo.providerId == EmailAuthProvider.PROVIDER_ID
  }
}