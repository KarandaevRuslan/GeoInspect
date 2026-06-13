package com.karandaev.geo_inspect.core.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

/**
 * Creates a [ViewModelProvider.Factory] for ViewModels that need [SavedStateHandle].
 */
inline fun <reified VM : ViewModel> savedStateViewModelFactory(
  crossinline createViewModel: (SavedStateHandle) -> VM
): ViewModelProvider.Factory {
  return object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
      modelClass: Class<T>,
      extras: CreationExtras
    ): T {
      if (modelClass.isAssignableFrom(VM::class.java)) {
        @Suppress("UNCHECKED_CAST")
        return createViewModel(extras.createSavedStateHandle()) as T
      }

      throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
  }
}