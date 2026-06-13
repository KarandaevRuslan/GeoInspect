package com.karandaev.geo_inspect.core.auth.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.karandaev.geo_inspect.core.auth.google.GoogleCredentialProvider
import kotlinx.coroutines.tasks.await

/**
 * Signs users into Firebase using Google Credential Manager.
 */
class FirebaseGoogleAuthProvider(
  private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

  /**
   * Opens Google account selection and signs the selected account into Firebase.
   */
  suspend fun signIn(context: Context) {
    val idToken = GoogleCredentialProvider(context).getIdToken()
    val credential = GoogleAuthProvider.getCredential(idToken, null)

    auth.signInWithCredential(credential).await()
  }
}