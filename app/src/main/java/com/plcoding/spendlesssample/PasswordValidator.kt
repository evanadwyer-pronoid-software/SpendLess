package com.plcoding.spendlesssample

enum class PasswordValidationResult {
    VALID,
    TOO_SHORT,
    MISSING_UPPERCASE,
    MISSING_LOWERCASE,
    MISSING_DIGIT,
}

fun validatePassword(password: String): PasswordValidationResult {
    if (password.length < 9) return PasswordValidationResult.TOO_SHORT
    if (password.none { it.isUpperCase() }) return PasswordValidationResult.MISSING_UPPERCASE
    if (password.none { it.isLowerCase() }) return PasswordValidationResult.MISSING_LOWERCASE
    if (password.none { it.isDigit() }) return PasswordValidationResult.MISSING_DIGIT
    return PasswordValidationResult.VALID
}
