package ege.bot.service.bot.keyboard_scenario.edit_student

import ege.bot.service.bot.keyboard_scenario.KeyboardButtonItem

enum class EditStudentKeyboard(
    private val text: String
): KeyboardButtonItem {

    CHECK_CURRENT_DATA("\uD83D\uDC40 Посмотреть текущие данные"),
    EXAM_TYPE("\uD83D\uDCD1 Тип экзамена (ЕГЭ/ОГЭ)"),
    YEAR("\uD83D\uDCC6 Год экзамена"),
    PASSPORT_SERIES("\uD83D\uDD24 Серия пасспорта"),
    PASSPORT_NUMBER("#\uFE0F⃣ Номер пасспорта"),
    SURNAME("✏\uFE0F Фамилия"),
    BACK("↩\uFE0F Назад");

    override fun getButtonText(): String {
        return text
    }
}