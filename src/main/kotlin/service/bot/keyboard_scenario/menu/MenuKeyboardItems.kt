package ege.bot.service.bot.keyboard_scenario.menu

import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.keyboard_scenario.KeyboardButtonItem

enum class MenuKeyboardItems(
    private val text: String,
    val requiredForChatMode: ChatMode? = null
): KeyboardButtonItem {

    CHECK_RESULTS("\uD83D\uDDDE\uFE0F Посмотреть результаты"),
    STUDENT("\uD83D\uDCC7 Данные ученика", ChatMode.STUDENT),
    STUDENTS("\uD83C\uDF93 Ученики", ChatMode.TEACHER),
    FORGET_ME("\uD83D\uDE48 Забыть меня");

    override fun getButtonText(): String {
        return text
    }
}