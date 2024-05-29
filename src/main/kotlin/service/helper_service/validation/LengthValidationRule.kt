package ege.bot.service.helper_service.validation

class LengthValidationRule(private val allowedLength: Int): ValidationRule {
    override fun <T> isValid(data: T): Boolean {
        return when(data) {
            is String -> data.length == allowedLength
            is Number -> data.toString().length == allowedLength
            else -> false
        }
    }
}