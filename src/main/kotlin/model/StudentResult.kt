package ege.bot.model

data class StudentResults(
    val result: String,
    val subject: String,
    val name: String?,
    val passportSeries: String,
    val passportNumber: String,
    val year: Short,
    val examType: ExamType
)