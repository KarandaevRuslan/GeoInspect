package com.karandaev.geo_inspect.core.auth.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.karandaev.geo_inspect.R

/**
 * Provides Google ID tokens using Android Credential Manager.
 */
class GoogleCredentialProvider(
  private val context: Context,
  private val credentialManager: CredentialManager = CredentialManager.create(context)
) {

  /**
   * Requests a Google ID token for Firebase authentication.
   *
   * @return Google ID token selected by the user.
   */
  suspend fun getIdToken(): String {
    val webClientId = context.getString(R.string.default_web_client_id)

    val googleIdOption = GetGoogleIdOption.Builder()
      .setServerClientId(webClientId)
      .setFilterByAuthorizedAccounts(false)
      .setAutoSelectEnabled(false)
      .build()

    val request = GetCredentialRequest.Builder()
      .addCredentialOption(googleIdOption)
      .build()

    val result = credentialManager.getCredential(
      context = context,
      request = request
    )

    val credential = GoogleIdTokenCredential.createFrom(result.credential.data)

    return credential.idToken
  }
}