package com.karandaev.geo_inspect.app.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val SplashDurationMillis = 1_500L

/**
 * Holds startup splash visibility.
 *
 * The timer starts only once for this ViewModel instance and survives configuration changes.
 */
class AppSplashViewModel : ViewModel() {

  private val _isSplashVisible = MutableStateFlow(true)

  /**
   * Whether startup splash should be shown.
   */
  val isSplashVisible: StateFlow<Boolean> = _isSplashVisible.asStateFlow()

  init {
    viewModelScope.launch {
      delay(SplashDurationMillis)
      _isSplashVisible.value = false
    }
  }
}