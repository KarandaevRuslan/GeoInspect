package com.karandaev.geo_inspect.core.auth.firebase

import android.content.Context
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

/**
 * Performs Firebase authentication and account operations.
 *
 * This class contains Firebase-specific logic only.
 * UI state, loading, messages, and input clearing should stay in ViewModels.
 */
class FirebaseAuthAccountManager(
  private val auth: FirebaseAuth,
  private val firebaseGoogleAuthProvider: FirebaseGoogleAuthProvider,
) {

  /**
   * Registers a new email/password user and sends verification email.
   *
   * The user is signed out after the verification email is sent.
   */
  suspend fun registerWithEmail(
    userName: String,
    email: String,
    password: String
  ) {
    auth.createUserWithEmailAndPassword(email, password).await()

    val user = requireCurrentUser()

    user.updateProfile(
      UserProfileChangeRequest.Builder()
        .setDisplayName(userName)
        .build()
    ).await()

    user.sendEmailVerification().await()

    auth.signOut()
  }

  /**
   * Signs in with email and password.
   *
   * Returns true when the email is verified.
   * If the email is not verified, sends a verification email and signs out.
   */
  suspend fun loginWithEmail(
    email: String,
    password: String
  ): Boolean {
    auth.signInWithEmailAndPassword(email, password).await()

    val user = requireCurrentUser()
    user.reload().await()

    if (!user.isEmailVerified) {
      user.sendEmailVerification().await()
      auth.signOut()
      return false
    }

    return true
  }

  /**
   * Sends password reset email.
   */
  suspend fun sendPasswordResetEmail(
    email: String
  ) {
    auth.sendPasswordResetEmail(email).await()
  }

  /**
   * Signs in with Google using Credential Manager and Firebase Auth.
   */
  suspend fun signInWithGoogle(
    context: Context
  ) {
    firebaseGoogleAuthProvider.signIn(context)
  }

  /**
   * Signs out from Firebase.
   */
  fun signOut() {
    auth.signOut()
  }

  /**
   * Updates current user's display name.
   */
  suspend fun updateCurrentUserName(
    userName: String
  ) {
    requireCurrentUser()
      .updateProfile(
        UserProfileChangeRequest.Builder()
          .setDisplayName(userName)
          .build()
      )
      .await()
  }

  /**
   * Changes password for email/password users.
   */
  suspend fun changePassword(
    newPassword: String
  ) {
    val user = requireCurrentUser()

    require(user.hasEmailPasswordProvider()) {
      "Password change is available only for email/password accounts."
    }

    user.updatePassword(newPassword).await()
  }

  /**
   * Deletes current signed-in account.
   */
  suspend fun deleteAccount() {
    requireCurrentUser().delete().await()
  }

  /**
   * Reauthenticates current email/password user with current password.
   *
   * This is required by Firebase before sensitive operations such as password change
   * or account deletion.
   */
  suspend fun reauthenticateWithPassword(
    password: String
  ) {
    val user = requireCurrentUser()

    require(user.hasEmailPasswordProvider()) {
      "Reauthentication with password is available only for email/password accounts."
    }

    val email = requireNotNull(user.email) {
      "Current user email is not available."
    }

    val credential = EmailAuthProvider.getCredential(
      email,
      password
    )

    user.reauthenticate(credential).await()
  }

  /**
   * Reloads current Firebase user.
   */
  suspend fun reloadCurrentUser() {
    auth.currentUser?.reload()?.await()
  }

  private fun requireCurrentUser(): FirebaseUser {
    return auth.currentUser ?: error("No signed-in user.")
  }
}