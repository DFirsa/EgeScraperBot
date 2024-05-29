package ege.bot.service.notification

import ege.bot.service.bot.TelegramBotService
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.dao.ResultDao
import ege.bot.service.dao.StudentDao
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.ResultExcelService
import ege.bot.service.helper_service.ResultsMessageCreatingService
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val resultDao: ResultDao,
    private val studentDao: StudentDao,
    private val excelService: ResultExcelService,
    private val chatService: ChatService,
    private val resultsMessageCreatingService: ResultsMessageCreatingService,
    private val telegramBotService: TelegramBotService
) {

    fun notifyUsers() {
        val studentIdToResults = resultDao.getNotReportedResults().groupBy { it.studentId!! }
        val uqStudentIds = studentIdToResults.keys.toSet()
        val chatIdToStudentsMap = studentDao.selectStudents(uqStudentIds)
            .groupBy { it.chatId }
            .mapValues { entry -> entry.value.groupBy { student -> student.id } }

        chatIdToStudentsMap.forEach{ (chatId, studentMap) ->
            val chatMode = chatService.getChat(chatId)?.chatMode ?: return@forEach

            if (chatMode == ChatMode.STUDENT) {
                val students = studentMap.flatMap { it.value }
                val resultsMap = studentIdToResults.filterKeys { it == students.first().id }
                val message = resultsMessageCreatingService.createTextMessage(resultsMap, students, chatId)
                telegramBotService.send(chatId, "$HEADER\n$message", null)

            } else {

                val students = studentMap.flatMap { it.value }
                val results = students.flatMap { studentIdToResults[it.id]!! }
                val excel = excelService.createExcel(studentMap, results)
                telegramBotService.send(chatId, HEADER, null)
                telegramBotService.send(chatId, CAPTION, excel)
            }
        }

        resultDao.markAsProcessed(studentIdToResults.flatMap { it.value })
    }

    companion object {
        private const val HEADER = "⚡ Появились новые результаты:\n"
        private const val CAPTION = "Новые результаты в файле"
    }
}