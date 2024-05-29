package ege.bot.model

data class Student(
    val id: Long = -1,
    var passportNumber: String,
    var passportSeries: String,
    var chatId: Long,
    var examType: ExamType,
    var surname: String,
    var examYear: Short
) {
    override fun toString(): String {
        return """
            |Экзамен: ${examType.name}
            |Год сдачи: $examYear
            |Серия паспорта: $passportSeries
            |Номер паспорта: $passportNumber
            |Фамилия: $surname
        """.trimMargin()
    }

    fun getStudentString(): String {
        return "*$surname* _(паспорт: $passportSeries $passportNumber)_  [${examType.name} - $examYear]"
    }
}
