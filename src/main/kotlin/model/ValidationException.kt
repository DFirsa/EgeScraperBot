package ege.bot.model

enum class ValidationException(
    val description: String
) {

    ARGUMENTS_COUNT("\uD83D\uDED1 Указано слишком мало параметров"),
    UNKNOWN_EXAM("\uD83D\uDED1 Некорректно указан тип экзамена"),
    INCORRECT_YEAR("\uD83D\uDED1 Некорректно указан год"),
    INCORRECT_SERIES("\uD83D\uDED1 Серия паспорта должна содержать только цифры"),
    INCORRECT_SERIES_LENGTH("\uD83D\uDED1 Некорректная длина серии паспорта"),
    INCORRECT_NUMBER("\uD83D\uDED1 Номер паспорта должна содержать только цифры"),
    INCORRECT_NUMBER_LENGTH("\uD83D\uDED1 Некорректная длина номера паспорта"),
    SURNAME_INCORRECT_CHARACTERS("\uD83D\uDED1 Фамилия содержит недопустимые символы")
}