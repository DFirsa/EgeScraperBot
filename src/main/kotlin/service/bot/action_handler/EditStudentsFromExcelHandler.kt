package ege.bot.service.bot.action_handler

import com.github.kotlintelegrambot.dispatcher.handlers.media.MediaHandlerEnvironment
import com.github.kotlintelegrambot.entities.files.Document
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.students.StudentsKeyboard
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.dao.ResultDao
import ege.bot.service.dao.StudentDao
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.StudentExcelService
import org.springframework.stereotype.Service

@Service
class EditStudentsFromExcelHandler(
    chatService: ChatService,
    private val studentsMessageCreator: KeyboardMessageCreator<StudentsKeyboard>,
    private val studentExcelService: StudentExcelService,
    private val studentDao: StudentDao,
    private val resultDao: ResultDao
): UserActionHandler<MediaHandlerEnvironment<Document>>(
    ChatState.LISTENING_FOR_UPDATED_STUDENTS,
    chatService,
    ChatState.STUDENTS_MENU
) {
    override fun processAction(context: MediaHandlerEnvironment<Document>) {
        val fileId = context.message.document?.fileId
        if (fileId == null) {
            sendTextMessage(context, FAILED_MESSAGE)
            return
        }

        val inputStream = context.bot.downloadFileBytes(fileId)!!.inputStream()
        val chatId = context.message.chat.id
        val chatStudents = studentDao.selectStudents(chatId)

        val studentsMap = try {
            studentExcelService.parseEditedStudents(inputStream, context.message.chat.id)
                .filter { chatStudents.map { student -> student.id }.toSet().contains(it.id) }
                .associateBy { it.id }
        }
        catch (e: Exception) {
            sendTextMessage(context, FAILED_MESSAGE)
            return
        }

        if (studentsMap.isEmpty()) {
            sendMessage(context.bot, chatId, NOTHING_IS_EDITED)
            return
        }

        if (studentsMap.size != chatStudents.size) {
            val message = chatStudents.filter { !studentsMap.containsKey(it.id) }
                    .joinToString("\n") { it.getStudentString() }
            sendMessage(context.bot, chatId, "${STUDENTS_IS_NOT_EDITED.trimMargin()}\n${message}")
        }

        resultDao.deleteResults(
            chatStudents.filter {
                val newData = studentsMap[it.id] ?: return@filter false
                newData.examType != it.examType
                        || newData.examYear != it.examYear
                        || newData.passportNumber != it.passportNumber
                        || newData.passportSeries != it.passportSeries
                        || newData.surname != it.surname
            }.map { it.id }
        )
        studentDao.update(studentsMap.values.toList())
        sendTextMessage(context, SUCCESS)
    }

    private fun sendTextMessage(context: MediaHandlerEnvironment<Document>, text: String) {
        sendMessage(
            context.bot,
            context.message.chat.id,
            text
        )
        sendMessage(
            context.bot,
            context.message.chat.id,
            studentsMessageCreator.getMessage(),
            studentsMessageCreator.getKeyboard(context.message.chat.id)
        )
    }

    companion object {
        private const val FAILED_MESSAGE = "Не удалось прочитать файл, попробуйте еще раз"
        private const val SUCCESS = "Данные обновлены ✅"
        private const val NOTHING_IS_EDITED = "❌ Данные об учениках не были изменены. Проверьте корректность переданной информации"
        private const val STUDENTS_IS_NOT_EDITED = """
            |⚠️ Данные некоторых учеников не изменены в связи с их некорректностью, перепроверьте данные и попробуйте снова.
            |Ученики: 
        """
    }

}