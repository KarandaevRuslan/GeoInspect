package com.karandaev.geo_inspect.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Creates a simple [ViewModelProvider.Factory] for ViewModels with manual dependencies.
 *
 * This helper removes repeated factory boilerplate from individual ViewModel classes.
 */
inline fun <reified VM : ViewModel> viewModelFactory(
  crossinline createViewModel: () -> VM
): ViewModelProvider.Factory {
  return object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(VM::class.java)) {
        @Suppress("UNCHECKED_CAST")
        return createViewModel() as T
      }

      throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
  }
}