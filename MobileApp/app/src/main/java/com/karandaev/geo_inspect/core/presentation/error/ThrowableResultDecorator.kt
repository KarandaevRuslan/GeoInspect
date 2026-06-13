package com.karandaev.geo_inspect.core.presentation.error

import kotlinx.coroutines.CancellationException

/**
 * Wraps throwable-producing operations into [Result].
 *
 * [CancellationException] is rethrown intentionally, because coroutine cancellation
 * should stay cooperative and should not be converted into UI errors.
 */
object ThrowableResultDecorator {

  /**
   * Runs synchronous operation and converts thrown errors into [Result.failure].
   *
   * @param block Operation body.
   */
  inline fun <T> run(
    block: () -> T
  ): Result<T> {
    return try {
      Result.success(block())
    } catch (error: CancellationException) {
      throw error
    } catch (error: Throwable) {
      Result.failure(error)
    }
  }

  /**
   * Runs suspending operation and converts thrown errors into [Result.failure].
   *
   * @param block Suspending operation body.
   */
  suspend fun <T> runSuspend(
    block: suspend () -> T
  ): Result<T> {
    return try {
      Result.success(block())
    } catch (error: CancellationException) {
      throw error
    } catch (error: Throwable) {
      Result.failure(error)
    }
  }
}