package ege.bot.service.helper_service.utils

import ege.bot.model.ExamType
import ege.bot.model.ValidationException
import ege.bot.service.helper_service.validation.LengthValidationRule
import ege.bot.service.helper_service.validation.NoDigitsValidationRule
import ege.bot.service.helper_service.validation.OnlyDigitsValidationRule

inline fun <T> validateAndMap(
    input: String,
    validationFunction: (String) -> ValidationException?,
    mappingFunction: (String) -> T,
    onValidation: (String, ValidationException) -> Nothing
): T {
    validationFunction.invoke(input)?.let { onValidation(input, it) }
    return mappingFunction.invoke(input)
}

fun validateYear(year: String): ValidationException? {
    if (!OnlyDigitsValidationRule().isValid(year)) {
        return ValidationException.INCORRECT_YEAR
    }

    if (LengthValidationRule(4).isValid(year.toShort())) {
        return null
    }

    return ValidationException.INCORRECT_YEAR
}

fun validateSeries(series: String): ValidationException? {
    if (!LengthValidationRule(4).isValid(series)) {
        return ValidationException.INCORRECT_SERIES_LENGTH
    }

    return if (OnlyDigitsValidationRule().isValid(series)) null else ValidationException.INCORRECT_SERIES
}

fun validateNumber(number: String): ValidationException? {
    if (!LengthValidationRule(6).isValid(number)) {
        return ValidationException.INCORRECT_NUMBER_LENGTH
    }

    return if (OnlyDigitsValidationRule().isValid(number)) null else ValidationException.INCORRECT_NUMBER
}

fun validateExam(exam: String): ValidationException? {
    if (ExamType.entries.map { it.name }.contains(exam)) {
        return null
    }
    return ValidationException.UNKNOWN_EXAM
}

fun validateSurname(surname: String): ValidationException? {
    if (NoDigitsValidationRule().isValid(surname)) {
        return null
    }

    return ValidationException.SURNAME_INCORRECT_CHARACTERS
}