package com.karandaev.geo_inspect.core.presentation.auth.validation

internal object PasswordStrengthCalculator {

  fun calculate(password: String): Int {
    if (password.isBlank()) return 0

    var score = 0

    if (password.length >= 6) score += 20
    if (password.length >= 8) score += 20
    if (password.length >= 12) score += 15

    if (password.any { it.isLowerCase() }) score += 10
    if (password.any { it.isUpperCase() }) score += 10
    if (password.any { it.isDigit() }) score += 15
    if (password.any { !it.isLetterOrDigit() }) score += 10

    return score.coerceIn(0, 100)
  }
}