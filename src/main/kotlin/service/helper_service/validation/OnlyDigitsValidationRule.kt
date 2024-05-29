package ege.bot.service.helper_service.validation

class OnlyDigitsValidationRule: ValidationRule {
    override fun <T> isValid(data: T): Boolean {
        return when (data) {
            is String -> data.all { it.isDigit() }
            is Number -> true
            else -> false
        }
    }
}