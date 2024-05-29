package ege.bot.service.bot.keyboard_scenario.students

import ege.bot.service.bot.keyboard_scenario.KeyboardButtonItem

enum class StudentsKeyboard(
    private val text: String
): KeyboardButtonItem {

    STUDENTS_DATA("\uD83D\uDC40 Посмотреть данные учеников"),
    EDIT_INFORMATION("✍\uFE0F Изменить информацию об учениках"),
    DELETE_STUDENTS("\uD83D\uDDD1\uFE0F Удалить информацию об учениках"),
    ADD_STUDENTS("➕Добавить учеников"),
    TO_MENU("↩\uFE0F В меню")
    ;

    override fun getButtonText(): String {
        return text
    }
}