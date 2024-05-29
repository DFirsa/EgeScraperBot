package ege.bot.service.helper_service

import ege.bot.model.Result
import ege.bot.model.Student
import ege.bot.model.StudentResults
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.dao.ResultDao
import org.springframework.stereotype.Service

@Service
class ResultsMessageCreatingService(
    private val resultDao: ResultDao,
    private val studentService: StudentService,
    private val chatService: ChatService
) {

    fun createTextMessage(resultsByStudents: Map<Long, List<Result>>, students: List<Student>, chatId: Long): String {
        val groupingData = students.flatMap { resultsByStudents[it.id]!!.map {
                result -> StudentResults(
            result.getResultText(),
            result.subject,
            it.surname,
            it.passportSeries,
            it.passportNumber,
            it.examYear,
            it.examType
        )
        } }
            .groupBy { it.year }
            .mapValues {
                    yearGroup -> yearGroup.value
                .groupBy { gr -> gr.examType }
                .mapValues {
                        examTypeGroup -> examTypeGroup.value.groupBy { gr -> gr.subject }
                    .mapValues {
                            subjectGroup -> subjectGroup.value
                        .map { res -> Pair(
                            res.name ?: (res.passportSeries + " " + res.passportNumber),
                            res.result
                        ) }
                        .sortedBy { pair -> pair.first}
                    }
                }
            }

        val chatMode = chatService.getChat(chatId)!!.chatMode
        return YOUR_RESULTS + "\n\n" + groupingData.map {
                (year, yearGroup) -> yearGroup.map {
                    (examType, examGr) ->  "*${examType.name}-$year:*\n$SPLITTER\n" + examGr.map {
                        (subject, results) -> if (chatMode == ChatMode.STUDENT) {
                            results.joinToString("\n") { nameToRes -> "$subject : ${nameToRes.second}" }
                        } else {
                            "$subject\n" + results.joinToString("\n") { nameToRes -> "${nameToRes.first} : ${nameToRes.second}" }
                        }
                    }.joinToString(if (chatMode == ChatMode.STUDENT) "\n" else "\n\n")
                }.joinToString("\n\n\n")
        }.joinToString("\n\n")
    }

    fun createTextMessage(chatId: Long): String {
        val students = studentService.listStudents(chatId).also {
            if (it.isEmpty()) return YOU_HAVE_NO_STUDENTS
        }
        val resultsByStudents = resultDao.getResults(chatId).also {
            if (it.isEmpty()) return NO_RESULTS_YET
        }.groupBy { it.studentId!! }

        return createTextMessage(resultsByStudents, students, chatId)
    }

    companion object {
        private const val YOUR_RESULTS = "\uD83D\uDDC2\uFE0F Результаты: "
        private const val YOU_HAVE_NO_STUDENTS = "Вы не добавляли информации об учениках \uD83E\uDD37\u200D♂\uFE0F"
        private const val NO_RESULTS_YET = "Похоже результатов еще нет \uD83E\uDD37\u200D♂\uFE0F"
        private const val SPLITTER = "---------------------"
    }
}

