package com.karandaev.geo_inspect.core.inspection_api

import com.google.firebase.auth.FirebaseAuth
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.domain.model.profile.MeProfile
import com.karandaev.geo_inspect.core.image.upload.ImageUploadFileProvider
import com.karandaev.geo_inspect.core.image.upload.ImageUploadFileProviders
import com.karandaev.geo_inspect.core.inspection_api.mapper.toDetectResponse
import com.karandaev.geo_inspect.core.inspection_api.mapper.toMeProfile
import com.karandaev.geo_inspect.core.inspection_api.remote.InspectionApi
import com.karandaev.geo_inspect.core.inspection_api.remote.InspectionApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Default implementation of [InspectionProvider] backed by the remote road crack backend.
 *
 * The provider prepares images, attaches Firebase authentication, calls Retrofit endpoints,
 * and maps remote DTOs to app domain models.
 *
 * The backend URL can be updated at runtime. When the URL changes, the Retrofit API
 * instance is recreated automatically.
 *
 * @param auth Firebase authentication provider.
 * @param initialBaseUrl Initial base URL of the road crack backend.
 * @param initialApi Optional initial Retrofit API.
 * @param detectImageUploadFileProvider Creates multipart upload files for detection images.
 * @param avatarImageUploadFileProvider Creates multipart upload files for profile avatars.
 */
class DefaultInspectionProvider(
  private val auth: FirebaseAuth,
  initialBaseUrl: String? = null,
  initialApi: InspectionApi? = initialBaseUrl?.let { baseUrl ->
    InspectionApiFactory.create(baseUrl = baseUrl)
  },
  private val detectImageUploadFileProvider: ImageUploadFileProvider =
    ImageUploadFileProviders.detect(),
  private val avatarImageUploadFileProvider: ImageUploadFileProvider =
    ImageUploadFileProviders.avatar()
) : InspectionProvider {

  /**
   * Current base URL of the road crack backend.
   *
   * Updating this value recreates the Retrofit API instance.
   */
  var baseUrl: String? = initialBaseUrl
    private set

  private var api: InspectionApi? = initialApi

  /**
   * Checks if base url is valid.
   *
   * @param baseUrl Backend base URL for validation.
   */
  override fun isValidBaseUrl(baseUrl: String): Boolean {
    return runCatching {
      InspectionApiFactory.create(baseUrl = baseUrl)
    }.isSuccess
  }

  /**
   * Updates the backend base URL and recreates the API instance.
   *
   * @param baseUrl New base URL of the road crack backend.
   *
   * @throws IllegalArgumentException if [baseUrl] is blank.
   */
  override fun updateBaseUrl(baseUrl: String) {
    require(baseUrl.isNotBlank()) {
      "Base URL must not be blank."
    }

    this.baseUrl = baseUrl
    this.api = InspectionApiFactory.create(
      baseUrl = baseUrl
    )
  }

  override fun clearBaseUrl() {
    this.baseUrl = null
    this.api = null
  }

  override suspend fun checkServerHealth(
    baseUrl: String
  ): Result<Boolean> {
    return runCatching {
      require(baseUrl.isNotBlank()) {
        "Base URL must not be blank."
      }

      withContext(Dispatchers.IO) {
        InspectionApiFactory.create(
          baseUrl = baseUrl
        )
          .health()
          .isSuccessful
      }
    }
  }

  override suspend fun checkServerHealth(): Result<Boolean> {
    return runCatching {
      withContext(Dispatchers.IO) {
        currentApi()
          .health()
          .isSuccessful
      }
    }
  }

  /**
   * Detects road cracks on the provided image.
   *
   * @throws IllegalStateException if the API is not initialized or user is not authenticated.
   */
  override suspend fun detect(
    sourcePath: String
  ): Result<DetectResponse> {
    return runCatching {
      withContext(Dispatchers.IO) {
        val uploadFile = detectImageUploadFileProvider.create(
          sourcePath = sourcePath
        )

        val responseDto = currentApi().detect(
          authorization = firebaseAuthHeader(),
          file = uploadFile.part
        )

        responseDto.toDetectResponse()
      }
    }
  }

  /**
   * Returns current authenticated backend user.
   *
   * @throws IllegalStateException if the API is not initialized or user is not authenticated.
   */
  override suspend fun me(): Result<MeProfile> {
    return runCatching {
      withContext(Dispatchers.IO) {
        currentApi().me(
          authorization = firebaseAuthHeader()
        ).toMeProfile()
      }
    }
  }

  /**
   * Returns current user's avatar URL, or null if no custom avatar is set.
   *
   * @throws IllegalStateException if the API is not initialized or user is not authenticated.
   */
  override suspend fun getAvatarUrl(): Result<String?> {
    return runCatching {
      withContext(Dispatchers.IO) {
        currentApi().getAvatar(
          authorization = firebaseAuthHeader()
        ).avatarUrl
      }
    }
  }

  /**
   * Uploads or replaces current user's avatar.
   *
   * @throws IllegalStateException if the API is not initialized or user is not authenticated.
   */
  override suspend fun updateAvatar(
    sourcePath: String
  ): Result<String?> {
    return runCatching {
      withContext(Dispatchers.IO) {
        val uploadFile = avatarImageUploadFileProvider.create(
          sourcePath = sourcePath
        )

        currentApi().updateAvatar(
          authorization = firebaseAuthHeader(),
          file = uploadFile.part
        ).avatarUrl
      }
    }
  }

  /**
   * Resets current user's avatar to default.
   *
   * @throws IllegalStateException if the API is not initialized or user is not authenticated.
   */
  override suspend fun resetAvatar(): Result<String?> {
    return runCatching {
      withContext(Dispatchers.IO) {
        currentApi().resetAvatar(
          authorization = firebaseAuthHeader()
        ).avatarUrl
      }
    }
  }

  /**
   * Downloads an avatar image by direct public URL.
   *
   * This method consumes and closes the Retrofit [okhttp3.ResponseBody].
   *
   * @param avatarUrl Public avatar URL returned by the backend.
   * @return Raw avatar image bytes.
   *
   * @throws IllegalArgumentException if [avatarUrl] is blank.
   * @throws IllegalStateException if the API is not initialized.
   */
  override suspend fun downloadAvatar(
    avatarUrl: String
  ): Result<ByteArray> {
    return runCatching {
      require(avatarUrl.isNotBlank()) {
        "Avatar URL must not be blank."
      }

      withContext(Dispatchers.IO) {
        currentApi()
          .downloadAvatar(avatarUrl = avatarUrl)
          .use { body ->
            body.bytes()
          }
      }
    }
  }

  /**
   * Gets current user's avatar URL and downloads avatar image bytes.
   *
   * @return Raw avatar image bytes, or null if no custom avatar is set.
   *
   * @throws IllegalStateException if the API is not initialized or user is not authenticated.
   */
  override suspend fun downloadCurrentAvatar(): Result<ByteArray?> {
    return runCatching {
      withContext(Dispatchers.IO) {
        val avatarUrl = currentApi().getAvatar(
          authorization = firebaseAuthHeader()
        ).avatarUrl

        if (avatarUrl.isNullOrBlank()) {
          null
        } else {
          currentApi()
            .downloadAvatar(avatarUrl = avatarUrl)
            .use { body ->
              body.bytes()
            }
        }
      }
    }
  }

  private fun currentApi(): InspectionApi {
    return api
      ?: error("Road crack backend API is not initialized. Set base URL before request.")
  }

  private suspend fun firebaseAuthHeader(): String {
    val token = auth.currentUser
      ?.getIdToken(false)
      ?.await()
      ?.token
      ?: error("Firebase user is not authenticated.")

    return token.asBearerToken()
  }

  private fun String.asBearerToken(): String {
    return "Bearer $this"
  }
}