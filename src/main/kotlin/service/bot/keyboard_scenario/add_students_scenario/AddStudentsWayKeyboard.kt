package ege.bot.service.bot.keyboard_scenario.add_students_scenario

import ege.bot.service.bot.keyboard_scenario.KeyboardButtonItem

enum class AddStudentsWayKeyboard(private val text: String): KeyboardButtonItem {

    EXCEL("\uD83D\uDCD2 Эксель таблица"),
    TEXT("\uD83D\uDCDD Текст в чате"),
    BACK("↩\uFE0F Вернуться");


    override fun getButtonText(): String {
        return text
    }
}