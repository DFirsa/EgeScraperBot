package ege.bot.service.helper_service.validation

interface ValidationRule {
    fun <T> isValid(data: T): Boolean
}