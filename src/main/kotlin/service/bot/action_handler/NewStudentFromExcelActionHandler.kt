package ege.bot.service.bot.action_handler

import com.github.kotlintelegrambot.dispatcher.handlers.media.MediaHandlerEnvironment
import com.github.kotlintelegrambot.entities.files.Document
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.students.StudentsKeyboard
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.StudentExcelService
import ege.bot.service.helper_service.StudentService
import org.springframework.stereotype.Service

@Service
class NewStudentFromExcelActionHandler(
    chatService: ChatService,
    private val studentsMenuMessageCreator: KeyboardMessageCreator<StudentsKeyboard>,
    private val studentExcelService: StudentExcelService,
    private val studentService: StudentService
): UserActionHandler<MediaHandlerEnvironment<Document>> (
    ChatState.LISTENING_FOR_NEW_STUDENTS_EXCEL,
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

        try {
            val lines = studentExcelService.parseExcelToLines(inputStream)
            val message = studentService.convertAndAdd(lines, context.message.chat.id)
            sendTextMessage(context, message)
        }
        catch (e: Exception) {
            sendTextMessage(context, FAILED_MESSAGE)
        }
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
            studentsMenuMessageCreator.getMessage(),
            studentsMenuMessageCreator.getKeyboard(context.message.chat.id)
        )
    }

    companion object {
        private const val FAILED_MESSAGE = "Не удалось прочитать файл, попробуйте еще раз"
    }

}