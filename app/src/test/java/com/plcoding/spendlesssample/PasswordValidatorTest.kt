package com.plcoding.spendlesssample

import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun nineCharsAllRulesMet_returnsValid() {
        assertEquals(PasswordValidationResult.VALID, validatePassword("Abcdefgh1"))
    }

    @Test
    fun longPasswordAllRulesMet_returnsValid() {
        val password = "Abcdefghij1".repeat(10)
        assertEquals(PasswordValidationResult.VALID, validatePassword(password))
    }

    @Test
    fun nineCharsWithSpecialChars_returnsValid() {
        assertEquals(PasswordValidationResult.VALID, validatePassword("Abc!@#de1"))
    }

    @Test
    fun passwordWithUnicodeUpperAndLower_returnsValid() {
        assertEquals(PasswordValidationResult.VALID, validatePassword("Ñoñoñoño1"))
    }

    @Test
    fun emptyString_returnsTooShort() {
        assertEquals(PasswordValidationResult.TOO_SHORT, validatePassword(""))
    }

    @Test
    fun singleCharacter_returnsTooShort() {
        assertEquals(PasswordValidationResult.TOO_SHORT, validatePassword("A"))
    }

    @Test
    fun eightCharsAllOtherRulesMet_returnsTooShort() {
        assertEquals(PasswordValidationResult.TOO_SHORT, validatePassword("Abcdefg1"))
    }

    @Test
    fun shortMissingUppercase_returnsTooShort() {
        assertEquals(PasswordValidationResult.TOO_SHORT, validatePassword("abcdef1"))
    }

    @Test
    fun shortMissingLowercase_returnsTooShort() {
        assertEquals(PasswordValidationResult.TOO_SHORT, validatePassword("ABCDEF1"))
    }

    @Test
    fun shortMissingDigit_returnsTooShort() {
        assertEquals(PasswordValidationResult.TOO_SHORT, validatePassword("Abcdefg"))
    }

    @Test
    fun shortMissingAllRules_returnsTooShort() {
        assertEquals(PasswordValidationResult.TOO_SHORT, validatePassword("!!!"))
    }

    @Test
    fun longEnoughNoUppercase_returnsMissingUppercase() {
        assertEquals(PasswordValidationResult.MISSING_UPPERCASE, validatePassword("abcdefgh1"))
    }

    @Test
    fun longEnoughOnlyDigits_returnsMissingUppercase() {
        assertEquals(PasswordValidationResult.MISSING_UPPERCASE, validatePassword("123456789"))
    }

    @Test
    fun longEnoughOnlyLowercase_returnsMissingUppercase() {
        assertEquals(PasswordValidationResult.MISSING_UPPERCASE, validatePassword("abcdefghi"))
    }

    @Test
    fun longEnoughOnlySpecials_returnsMissingUppercase() {
        assertEquals(PasswordValidationResult.MISSING_UPPERCASE, validatePassword("!@#\$%^&*("))
    }

    @Test
    fun nineSpaces_returnsMissingUppercase() {
        assertEquals(PasswordValidationResult.MISSING_UPPERCASE, validatePassword("         "))
    }

    @Test
    fun longEnoughNoLowercase_returnsMissingLowercase() {
        assertEquals(PasswordValidationResult.MISSING_LOWERCASE, validatePassword("ABCDEFGH1"))
    }

    @Test
    fun longEnoughUpperAndSpecials_returnsMissingLowercase() {
        assertEquals(PasswordValidationResult.MISSING_LOWERCASE, validatePassword("ABCDEFGH!"))
    }

    @Test
    fun longEnoughOnlyUppercase_returnsMissingLowercase() {
        assertEquals(PasswordValidationResult.MISSING_LOWERCASE, validatePassword("ABCDEFGHI"))
    }

    @Test
    fun longEnoughNoDigit_returnsMissingDigit() {
        assertEquals(PasswordValidationResult.MISSING_DIGIT, validatePassword("Abcdefghi"))
    }

    @Test
    fun longEnoughLettersAndSpecials_returnsMissingDigit() {
        assertEquals(PasswordValidationResult.MISSING_DIGIT, validatePassword("Abcdef!gh"))
    }
}
