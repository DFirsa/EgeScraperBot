package ege.bot.service.helper_service.validation

class NoDigitsValidationRule: ValidationRule {
    override fun <T> isValid(data: T): Boolean {
        if (data !is String) {
            return false
        }

        return data.all { it.isLetter() || it == ' ' }
    }
}