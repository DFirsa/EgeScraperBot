package ege.bot.service.bot.keyboard_scenario.teacher_print_result

import ege.bot.service.bot.keyboard_scenario.KeyboardButtonItem

enum class PrintResultKeyboard(
    private val text: String
): KeyboardButtonItem {

    EXCEL_FILE("\uD83D\uDCC3 Результаты в excel-файле"),
    TEXT_IN_CHAT("\uD83D\uDCDD Результаты текстом в чате");
    override fun getButtonText(): String {
        return text
    }
}