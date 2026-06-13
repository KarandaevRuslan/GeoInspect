package com.karandaev.geo_inspect.core.inspection_api.remote

import com.karandaev.geo_inspect.core.inspection_api.dto.detect.DetectResponseDto
import com.karandaev.geo_inspect.core.inspection_api.dto.profile.AvatarResponseDto
import com.karandaev.geo_inspect.core.inspection_api.dto.profile.MeResponseDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

/**
 * Retrofit API for road crack detection backend endpoints.
 */
interface InspectionApi {

  /**
   * Checks whether backend server is reachable.
   */
  @GET(InspectionEndpoints.HealthPath)
  suspend fun health(): Response<Unit>

  /**
   * Uploads an image and returns detected road cracks.
   *
   * @param authorization Optional Firebase bearer token.
   * @param file Image file uploaded as multipart/form-data.
   */
  @Multipart
  @POST(InspectionEndpoints.DetectPath)
  suspend fun detect(
    @Header("Authorization") authorization: String?,
    @Part file: MultipartBody.Part
  ): DetectResponseDto

  /**
   * Returns the current authenticated Firebase user.
   *
   * @param authorization Firebase bearer token.
   */
  @GET(InspectionEndpoints.MePath)
  suspend fun me(
    @Header("Authorization") authorization: String?
  ): MeResponseDto

  /**
   * Returns current user's avatar URL.
   *
   * @param authorization Firebase bearer token.
   */
  @GET(InspectionEndpoints.ProfileAvatarPath)
  suspend fun getAvatar(
    @Header("Authorization") authorization: String?
  ): AvatarResponseDto

  /**
   * Uploads or replaces current user's avatar.
   *
   * @param authorization Firebase bearer token.
   * @param file Avatar image file uploaded as multipart/form-data.
   */
  @Multipart
  @POST(InspectionEndpoints.ProfileAvatarPath)
  suspend fun updateAvatar(
    @Header("Authorization") authorization: String?,
    @Part file: MultipartBody.Part
  ): AvatarResponseDto

  /**
   * Resets current user's avatar to default.
   *
   * @param authorization Firebase bearer token.
   */
  @DELETE(InspectionEndpoints.ProfileAvatarPath)
  suspend fun resetAvatar(
    @Header("Authorization") authorization: String?
  ): AvatarResponseDto

  /**
   * Downloads an avatar image by absolute public URL.
   *
   * Authorization is not required because backend serves \public\avatars** publicly.
   */
  @GET
  suspend fun downloadAvatar(
    @Url avatarUrl: String
  ): ResponseBody
}