package com.karandaev.geo_inspect.core.image.upload

import okhttp3.MultipartBody

/**
 * File prepared for multipart upload.
 *
 * @param part Multipart file part used by Retrofit.
 */
data class ImageUploadFile(
  val part: MultipartBody.Part
)