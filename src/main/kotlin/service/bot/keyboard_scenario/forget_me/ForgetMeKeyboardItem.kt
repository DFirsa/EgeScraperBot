package ege.bot.service.bot.keyboard_scenario.forget_me

import ege.bot.service.bot.keyboard_scenario.KeyboardButtonItem

enum class ForgetMeKeyboardItem(private val text: String): KeyboardButtonItem {

    I_AM_SURE("✅ Да, удалить"),
    I_AM_NOT_SURE("❌ Нет, не удалять");

    override fun getButtonText(): String {
        return text
    }
}