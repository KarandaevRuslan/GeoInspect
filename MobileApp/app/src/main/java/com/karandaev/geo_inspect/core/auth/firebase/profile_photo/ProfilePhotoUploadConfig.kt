package com.karandaev.geo_inspect.core.auth.firebase.profile_photo

/**
 * Configuration for Firebase profile photo upload.
 *
 * Source limits are used before compression.
 * Output limits are used after compression, before Firebase Storage upload.
 */
data class ProfilePhotoUploadConfig(
  val maxSourceBytes: Long = 10L * 1024L * 1024L,
  val maxSourceWidth: Int = 4096,
  val maxSourceHeight: Int = 4096,
  val maxSourceMegapixels: Int = 20,
  val maxOutputBytes: Long = 512L * 1024L,
  val maxOutputSidePx: Int = 512,
  val outputJpegQuality: Int = 82,
  val storageDirectory: String = "profile_photos",
  val storageFileName: String = "avatar.jpg"
)