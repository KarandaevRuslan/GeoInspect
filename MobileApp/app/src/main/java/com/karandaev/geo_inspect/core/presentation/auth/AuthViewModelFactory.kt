package com.karandaev.geo_inspect.core.presentation.auth

import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.karandaev.geo_inspect.core.auth.firebase.FirebaseAuthAccountManager
import com.karandaev.geo_inspect.core.auth.firebase.FirebaseGoogleAuthProvider
import com.karandaev.geo_inspect.core.domain.repository.PersistedAppStateRepository
import com.karandaev.geo_inspect.core.image.crop.ImageCropper
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFileProvider
import com.karandaev.geo_inspect.core.presentation.viewModelFactory
import com.karandaev.geo_inspect.core.inspection_api.InspectionProvider

/**
 * Creates an [AuthViewModel].
 */
fun AuthViewModel.Companion.factory(
  auth: FirebaseAuth,
  firebaseGoogleAuthProvider: FirebaseGoogleAuthProvider,
  persistedAppStateRepository: PersistedAppStateRepository,
  inspectionProvider: InspectionProvider,
  imageSourceFileProvider: ImageSourceFileProvider,
  imageCropper: ImageCropper
): ViewModelProvider.Factory {
  return viewModelFactory {
    AuthViewModel(
      auth = auth,
      authAccountManager = FirebaseAuthAccountManager(
        auth = auth,
        firebaseGoogleAuthProvider = firebaseGoogleAuthProvider
      ),
      persistedAppStateRepository = persistedAppStateRepository,
      inspectionProvider = inspectionProvider,
      imageSourceFileProvider = imageSourceFileProvider,
      imageCropper = imageCropper
    )
  }
}