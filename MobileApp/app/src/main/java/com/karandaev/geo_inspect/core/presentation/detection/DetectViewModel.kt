package com.karandaev.geo_inspect.core.presentation.detection

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karandaev.geo_inspect.core.domain.model.detection.DetectResponse
import com.karandaev.geo_inspect.core.image.crop.ImageCropper
import com.karandaev.geo_inspect.core.image.crop.NormalizedCropBox
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFile
import com.karandaev.geo_inspect.core.image.source_file.ImageSourceFileProvider
import com.karandaev.geo_inspect.core.inspection_api.InspectionProvider
import com.karandaev.geo_inspect.core.presentation.error.ThrowableResultDecorator
import com.karandaev.geo_inspect.core.presentation.message.UiMessage
import com.karandaev.geo_inspect.core.presentation.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel that manages image selection, crop state and infrastructure damage detection.
 *
 * @param inspectionProvider Provider used to run remote detection requests.
 * @param imageSourceFileProvider Provider used to copy external image URIs into app-local files.
 * @param imageCropper Cropper used to prepare selected images before detection.
 */
class DetectViewModel(
  private val inspectionProvider: InspectionProvider,
  private val imageSourceFileProvider: ImageSourceFileProvider,
  private val imageCropper: ImageCropper
) : ViewModel() {

  // -----------------------------------------------------------------------------------------------
  // State
  // -----------------------------------------------------------------------------------------------

  private val _state = MutableStateFlow(
    DetectUiState()
  )

  /**
   * Current immutable detection UI state.
   */
  val state: StateFlow<DetectUiState> = _state.asStateFlow()

  private val _completedResult = MutableStateFlow<DetectCompletedResult?>(null)

  /**
   * Last successfully completed detection result.
   *
   * This value is used by create/edit route to copy detection result into form state.
   * It is intentionally separated from [state], because [state] is screen UI state.
   */
  val completedResult: StateFlow<DetectCompletedResult?> = _completedResult.asStateFlow()

  private var detectJob: Job? = null

  private var nextRefreshId = 0L
  private var nextMessageRefreshId = 0L

  // -----------------------------------------------------------------------------------------------
  // Gallery image actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Selects image from gallery and moves detection flow directly to crop step.
   *
   * @param context Android context used to copy selected image into app-local storage.
   * @param uri Selected image URI, or null when gallery selection was cancelled.
   */
  fun setGalleryImage(
    context: Context,
    uri: Uri?
  ) {
    setImageFromUri(
      context = context,
      uri = uri,
      nullUriMessage = DetectUiMessages.GallerySelectionCancelled
    )
  }

  // -----------------------------------------------------------------------------------------------
  // Camera image actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Stores captured camera photo and moves detection flow directly to crop step.
   *
   * @param context Android context used to copy captured image into app-local storage.
   * @param uri Captured photo URI, or null when camera capture was cancelled.
   */
  fun setCapturedPhoto(
    context: Context,
    uri: Uri?
  ) {
    setImageFromUri(
      context = context,
      uri = uri,
      nullUriMessage = DetectUiMessages.CameraCaptureCancelled
    )
  }

  /**
   * Shows an informational message when camera capture was cancelled.
   */
  fun onCameraCaptureCancelled() {
    showInfo(DetectUiMessages.CameraCaptureCancelled)
  }

  /**
   * Shows an error message when camera permission was denied.
   */
  fun onCameraPermissionDenied() {
    showError(DetectUiMessages.CameraPermissionDenied)
  }

  // -----------------------------------------------------------------------------------------------
  // Crop actions
  // -----------------------------------------------------------------------------------------------

  fun setCropBox(
    cropBox: NormalizedCropBox?
  ) {
    if (isDetectionActive()) {
      return
    }

    val imageSource = currentImageSource() ?: return

    if (imageSource.cropBox == cropBox) {
      return
    }

    _state.update { state ->
      state.copy(
        imageSource = imageSource.withCropBox(cropBox),
        detectResponse = null,
      )
    }
  }

  /**
   * Resets selected image crop and uses original image for detection.
   */
  fun resetCrop() {
    setCropBox(null)
  }

  // -----------------------------------------------------------------------------------------------
  // Clear actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Clears selected image, crop state, messages and previous detection result.
   */
  fun clearSelectedImage() {
    if (isDetectionActive()) {
      return
    }

    _state.value = DetectUiState()
  }

  /**
   * Clears current UI message.
   */
  fun clearMessage() {
    _state.update { state ->
      state.copy(
        message = null
      )
    }
  }

  /**
   * Clears current detection flow state before opening detection screen again.
   *
   * This is needed because [DetectViewModel] is shared between create and detect routes.
   * The completed handoff result is also cleared so a new detection run cannot re-apply
   * an older result to the create/edit form.
   */
  fun resetForNewDetection() {
    detectJob?.cancel()
    detectJob = null

    _state.value = DetectUiState()
    _completedResult.value = null
  }

  /**
   * Clears completed detection result after it has been copied into another route state.
   *
   * The [refreshId] guard prevents clearing a newer result from an older effect.
   *
   * @param refreshId Completed result id that was consumed.
   */
  fun consumeCompletedResult(
    refreshId: Long
  ) {
    val currentResult = _completedResult.value

    if (currentResult?.refreshId != refreshId) {
      return
    }

    _completedResult.value = null
  }

  // -----------------------------------------------------------------------------------------------
  // Detection actions
  // -----------------------------------------------------------------------------------------------

  /**
   * Runs detection for the currently selected image.
   *
   * If a crop box is selected, image is cropped before inference.
   *
   * @param context Android context used to create cropped image file when needed.
   */
  fun detectSelectedImage(
    context: Context
  ) {
    if (isDetectionActive()) {
      return
    }

    val imageSource = currentImageSource()

    if (imageSource == null) {
      showError(DetectUiMessages.NoImageSelected)
      return
    }

    startDetection(
      context = context,
      imageSource = imageSource
    )
  }

  // -----------------------------------------------------------------------------------------------
  // Image source helpers
  // -----------------------------------------------------------------------------------------------

  private fun setImageFromUri(
    context: Context,
    uri: Uri?,
    nullUriMessage: String
  ) {
    if (isDetectionActive()) {
      return
    }

    if (uri == null) {
      showInfo(nullUriMessage)
      return
    }

    ThrowableResultDecorator
      .run {
        createImageSource(
          context = context,
          uri = uri
        )
      }
      .onSuccess { imageSource ->
        showImageStep(
          imageSource = imageSource,
          step = DetectStep.CropImage
        )
      }
      .onFailure { error ->
        showError(error)
      }
  }

  /**
   * Copies external image URI into app-local image source file.
   *
   * @param context Android context used to copy image.
   * @param uri External image URI.
   */
  private fun createImageSource(
    context: Context,
    uri: Uri
  ): ImageSourceFile {
    return imageSourceFileProvider.createSourceFile(
      context = context,
      uri = uri
    )
  }

  /**
   * Switches UI to image-related step with provided image source.
   *
   * @param imageSource Selected image source.
   * @param step Target detection flow step.
   */
  private fun showImageStep(
    imageSource: ImageSourceFile,
    step: DetectStep
  ) {
    _state.value = DetectUiState(
      step = step,
      imageSource = imageSource
    )
  }

  /**
   * Returns currently selected image source, if any.
   */
  private fun currentImageSource(): ImageSourceFile? {
    return _state.value.imageSource
  }

  /**
   * Returns image source with updated crop box and original source path restored.
   *
   * @param cropBox New crop box, or null to use original image.
   */
  private fun ImageSourceFile.withCropBox(
    cropBox: NormalizedCropBox?
  ): ImageSourceFile {
    return copy(
      sourcePath = originalSourcePath,
      cropBox = cropBox
    )
  }

  // -----------------------------------------------------------------------------------------------
  // Detection pipeline helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Starts asynchronous detection pipeline.
   *
   * Pipeline prepares upload image, runs backend request and applies result to UI state.
   * Crop errors and backend errors are handled by one decorator.
   *
   * @param context Android context used to create cropped image file when needed.
   * @param imageSource Selected image source.
   */
  private fun startDetection(
    context: Context,
    imageSource: ImageSourceFile
  ) {
    showLoading()

    detectJob = viewModelScope.launch {
      try {
        val result = ThrowableResultDecorator.runSuspend {
          runDetectionPipeline(
            context = context,
            imageSource = imageSource
          )
        }

        if (!isActive) {
          return@launch
        }

        result
          .onSuccess { pipelineResult ->
            showDetectionSuccess(
              imageSource = pipelineResult.imageSource,
              data = pipelineResult.detectResponse
            )
          }
          .onFailure { error ->
            showDetectionError(
              imageSource = imageSource,
              error = error
            )
          }
      } finally {
        detectJob = null
      }
    }
  }

  /**
   * Returns original or cropped image source that should be uploaded for detection.
   *
   * @param context Android context used by image cropper.
   * @param imageSource Selected image source.
   */
  private suspend fun prepareDetectionImageForUpload(
    context: Context,
    imageSource: ImageSourceFile
  ): ImageSourceFile {
    val cropBox = imageSource.cropBox ?: return imageSource

    return withContext(Dispatchers.Default) {
      imageCropper.crop(
        context = context,
        imageSource = imageSource.copy(
          sourcePath = imageSource.originalSourcePath
        ),
        cropBox = cropBox
      )
    }
  }

  /**
   * Successful detection pipeline result.
   *
   * @param imageSource Image source actually sent to backend.
   * @param detectResponse Backend detection response.
   */
  private data class DetectionPipelineResult(
    val imageSource: ImageSourceFile,
    val detectResponse: DetectResponse
  )

  /**
   * Runs full detection pipeline.
   *
   * The pipeline includes optional image crop and backend detection request.
   * Any thrown error is handled by [ThrowableResultDecorator] in [startDetection].
   *
   * @param context Android context used by cropper.
   * @param imageSource Selected image source.
   */
  private suspend fun runDetectionPipeline(
    context: Context,
    imageSource: ImageSourceFile
  ): DetectionPipelineResult {
    val uploadImageSource = prepareDetectionImageForUpload(
      context = context,
      imageSource = imageSource
    )

    val detectResponse = inspectionProvider
      .detect(
        sourcePath = uploadImageSource.sourcePath
      )
      .getOrThrow()

    return DetectionPipelineResult(
      imageSource = uploadImageSource,
      detectResponse = detectResponse
    )
  }

  /**
   * Stores successful detection result and marks it as not delivered to route delegate yet.
   *
   * @param imageSource Image source used for detection.
   * @param data Detection response.
   */
  private fun showDetectionSuccess(
    imageSource: ImageSourceFile,
    data: DetectResponse
  ) {
    val refreshId = nextRefreshId()

    _completedResult.value = DetectCompletedResult(
      refreshId = refreshId,
      imageSource = imageSource,
      detectResponse = data
    )

    _state.update { state ->
      state.copy(
        step = DetectStep.CropImage,
        imageSource = imageSource,
        detectResponse = data,
        isLoading = false,
        message = null
      )
    }
  }

  /**
   * Stores failed detection result as user-friendly error message.
   *
   * @param imageSource Image source used for detection attempt.
   * @param error Detection failure.
   */
  private fun showDetectionError(
    imageSource: ImageSourceFile,
    error: Throwable
  ) {
    _state.update { state ->
      state.copy(
        step = DetectStep.CropImage,
        imageSource = imageSource,
        detectResponse = null,
        isLoading = false,
        message = createErrorMessage(
          text = DetectErrorMapper.toMessage(error)
        )
      )
    }
  }

  // -----------------------------------------------------------------------------------------------
  // UI state helpers
  // -----------------------------------------------------------------------------------------------

  /**
   * Switches UI state to loading and clears previous message.
   */
  private fun showLoading() {
    _state.update { state ->
      state.copy(
        isLoading = true,
        message = null
      )
    }
  }

  /**
   * Shows non-error UI message.
   *
   * @param message Message text.
   */
  private fun showInfo(
    message: String
  ) {
    showMessage(
      message = createInfoMessage(message)
    )
  }

  /**
   * Shows error UI message.
   *
   * @param message Error message text.
   */
  private fun showError(
    message: String
  ) {
    showMessage(
      message = createErrorMessage(message)
    )
  }

  /**
   * Shows throwable as mapped error UI message.
   *
   * @param error Error to map.
   */
  private fun showError(
    error: Throwable
  ) {
    showError(
      message = DetectErrorMapper.toMessage(error)
    )
  }

  /**
   * Shows UI message.
   *
   * @param message Message to display.
   */
  private fun showMessage(
    message: UiMessage
  ) {
    _state.update { state ->
      state.copy(
        message = message
      )
    }
  }

  /**
   * Creates informational UI message with fresh refresh id.
   */
  private fun createInfoMessage(
    text: String
  ): UiMessage {
    return UiMessage.info(
      text = text,
      refreshId = nextMessageRefreshId()
    )
  }

  /**
   * Creates error UI message with fresh refresh id.
   */
  private fun createErrorMessage(
    text: String
  ): UiMessage {
    return UiMessage.error(
      text = text,
      refreshId = nextMessageRefreshId()
    )
  }

  /**
   * Returns next monotonically increasing message refresh id.
   */
  private fun nextMessageRefreshId(): Long {
    nextMessageRefreshId += 1L
    return nextMessageRefreshId
  }

  /**
   * Returns whether detection coroutine is currently active.
   */
  private fun isDetectionActive(): Boolean {
    return detectJob?.isActive == true
  }

  /**
   * Returns next monotonically increasing refresh id.
   */
  private fun nextRefreshId(): Long {
    nextRefreshId += 1L
    return nextRefreshId
  }

  // -----------------------------------------------------------------------------------------------
  // Factory
  // -----------------------------------------------------------------------------------------------

  companion object {

    /**
     * Creates [DetectViewModel] factory.
     *
     * @param inspectionProvider Provider used to run remote detection requests.
     * @param imageSourceFileProvider Provider used to copy selected images into app-local files.
     * @param imageCropper Cropper used to prepare selected image before detection.
     */
    fun factory(
      inspectionProvider: InspectionProvider,
      imageSourceFileProvider: ImageSourceFileProvider,
      imageCropper: ImageCropper
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        DetectViewModel(
          inspectionProvider = inspectionProvider,
          imageSourceFileProvider = imageSourceFileProvider,
          imageCropper = imageCropper
        )
      }
    }
  }
}