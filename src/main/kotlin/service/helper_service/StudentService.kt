package ege.bot.service.helper_service

import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ExamType
import ege.bot.model.Student
import ege.bot.model.ValidationException
import ege.bot.service.dao.ResultDao
import ege.bot.service.dao.StudentDao
import ege.bot.service.helper_service.utils.*
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentDao: StudentDao,
    private val resultDao: ResultDao
) {

    fun delete(ids: List<Long>) {
        studentDao.deleteStudents(ids)
    }

    fun deleteAll(chatId: Long) {
        val idsToDelete = studentDao.selectStudents(chatId).map { it.id }
        delete(idsToDelete)
    }

    fun deleteByOrders(orderIds: List<Int>, chatId: Long) {
        val studentIds = studentDao.selectStudentsByOrder(orderIds, chatId).map { it.id }
        studentDao.deleteStudents(studentIds)
    }

    /**
     * Convert text to models and save. Message to sending will be returned
     */
    fun convertAndAdd(text: List<String>, chatId: Long): String {
        val convertedData = text.map { convertToModel(it, chatId) }
        val incorrectRows = convertedData.filter { it.second != null }
            .groupBy { it.second }
            .mapValues { it.value.map { triple -> triple.first } }

        val studentsToSave = convertedData.mapNotNull { it.third }
        studentDao.insertStudents(studentsToSave)
        return buildOutputMessage(incorrectRows, studentsToSave.isNotEmpty())
    }

    private fun buildOutputMessage(wrongRowsMap: Map<ValidationException?, List<String>>, savedAny: Boolean): String {
        val wrongLines =  wrongRowsMap.map {
            val wrongRows = it.value.joinToString("\n")
            it.key!!.description + "\n" + wrongRows
        }.joinToString("\n\n")

        if (wrongLines.isNotEmpty()) {
            return "${CONTAINS_WRONG_LINES}\n\n${wrongLines}\n\n${ if (savedAny) SAVED_WITH_EXCEPTIONS else "" }"
        }

        return SAVED
    }

    fun switchExamType(textHandlerEnvironment: TextHandlerEnvironment) {
        val student = studentDao.selectStudents(textHandlerEnvironment.message.chat.id).first()
        student.examType = if (student.examType == ExamType.EGE) ExamType.OGE else ExamType.EGE
        studentDao.update(listOf(student))
        resultDao.deleteResults(listOf(student.id))
    }

    fun updateExamYear(textHandlerEnvironment: TextHandlerEnvironment): ValidationException? {
        val year = validateAndMap(
            textHandlerEnvironment.text.lines().first().trim(),
            ::validateYear,
            { it.toShort() },
            { _, exception -> return exception }
        )

        val student = studentDao.selectStudents(textHandlerEnvironment.message.chat.id).first()
        student.examYear = year
        studentDao.update(listOf(student))
        resultDao.deleteResults(listOf(student.id))
        return null
    }

    fun updateSeriesOrNumber(textHandlerEnvironment: TextHandlerEnvironment, isSeries: Boolean): ValidationException? {
        val value = validateAndMap(
            textHandlerEnvironment.text.lines().first().trim(),
            if (isSeries ) ::validateSeries else ::validateNumber,
            { it },
            { _, exception -> return exception }
        )
        val student = studentDao.selectStudents(textHandlerEnvironment.message.chat.id).first()
        if (isSeries) {
            student.passportSeries = value
        } else {
            student.passportNumber = value
        }
        studentDao.update(listOf(student))
        resultDao.deleteResults(listOf(student.id))
        return null
    }

    fun updateSurname(textHandlerEnvironment: TextHandlerEnvironment): ValidationException? {
        val result = validateAndMap(
            textHandlerEnvironment.text.lines().first().trim(),
            ::validateSurname,
            { it },
            { _, exception -> return exception }
        )

        val student = studentDao.selectStudents(textHandlerEnvironment.message.chat.id).first()
        student.surname = result
        studentDao.update(listOf(student))
        resultDao.deleteResults(listOf(student.id))
        return null
    }

    fun listStudents(chatId: Long): List<Student> {
        return studentDao.selectStudents(chatId)
    }

    /**
     * Validate text row to pattern and if text is valid converts it to model:
     *
     * Pattern:
     * <exam type> <year> <pass number> <pass code> <name and surname>
     * where <name and surname> is optional
     */
    private fun convertToModel(text: String, chatId: Long): Triple<String, ValidationException?, Student?> {
        val parts = text.split(" ")
            .also { if (it.size < 5) return Triple(text, ValidationException.ARGUMENTS_COUNT, null) }

        val examType = validateAndMap(parts.first(), ::validateExam, { ExamType.valueOf(it) }) {
            input, exception -> return Triple(input, exception, null)
        }
        val year = validateAndMap(parts[1], ::validateYear, { it.toShort() } ) {
                input, exception -> return Triple(input, exception, null)
        }
        val series = validateAndMap(parts[2], ::validateSeries, { it } ) {
                input, exception -> return Triple(input, exception, null)
        }
        val number = validateAndMap(parts[3], ::validateNumber, { it } ) {
                input, exception -> return Triple(input, exception, null)
        }
        val surnameStr = parts.subList(4, parts.size).joinToString(separator = " ")
        val surname = validateAndMap(
            surnameStr,
            ::validateSurname,
            { it }
        ) {
            input, exception -> return Triple(input, exception, null)
        }

        return Triple(
            text, null, Student(
                passportNumber = number,
                passportSeries = series,
                chatId = chatId,
                examType = examType,
                examYear = year,
                surname = surname
            )
        )
    }

    companion object {
        private const val CONTAINS_WRONG_LINES = "В указанных данных есть ошибки:"
        private const val SAVED_WITH_EXCEPTIONS = "Остальные строчки были сохранены!"
        private const val SAVED = "Данные успешно сохранены"
    }
}