package ege.bot.service.bot.keyboard_scenario.edit_student

import com.github.kotlintelegrambot.dispatcher.handlers.HandleText
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.OnClickHandler
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.keyboard_scenario.students.StudentsMessageCreator
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.StudentService
import org.springframework.stereotype.Service

@Service
class EditStudentOnClickHandler(
    chatService: ChatService,
    private val studentsMessageCreator: StudentsMessageCreator,
    private val studentService: StudentService,
    private val editStudentMessageCreator: KeyboardMessageCreator<EditStudentKeyboard>
): OnClickHandler<EditStudentKeyboard>(EditStudentKeyboard::class.java, ChatState.EDIT_STUDENT_MENU, chatService) {
    override fun getHandlerFunction(button: EditStudentKeyboard): HandleText {
        return when(button) {
            EditStudentKeyboard.EXAM_TYPE -> this::switchExamType
            EditStudentKeyboard.YEAR -> sendTextMessage(YEAR_MESSAGE)
            EditStudentKeyboard.PASSPORT_SERIES -> sendTextMessage(SERIES_MESSAGE)
            EditStudentKeyboard.PASSPORT_NUMBER -> sendTextMessage(NUMBER_MESSAGE)
            EditStudentKeyboard.BACK -> runMessageScenario(studentsMessageCreator)
            EditStudentKeyboard.CHECK_CURRENT_DATA -> this::checkCurrentData
            EditStudentKeyboard.SURNAME -> sendTextMessage(SURNAME_MESSAGE)
        }
    }

    override fun getNextState(button: EditStudentKeyboard, chatMode: ChatMode?, chatId: Long?): ChatState? {
        return when(button) {
            EditStudentKeyboard.EXAM_TYPE -> ChatState.EDIT_STUDENT_MENU
            EditStudentKeyboard.YEAR -> ChatState.EDIT_YEAR
            EditStudentKeyboard.PASSPORT_SERIES -> ChatState.EDIT_PASSPORT_SERIES
            EditStudentKeyboard.PASSPORT_NUMBER -> ChatState.EDIT_PASSPORT_NUMBER
            EditStudentKeyboard.BACK -> ChatState.STUDENTS_MENU
            EditStudentKeyboard.CHECK_CURRENT_DATA -> ChatState.EDIT_STUDENT_MENU
            EditStudentKeyboard.SURNAME -> ChatState.EDIT_SURNAME
        }
    }

    fun switchExamType(textHandlerEnvironment: TextHandlerEnvironment) {
        studentService.switchExamType(textHandlerEnvironment)
        sendMessage(textHandlerEnvironment.bot, textHandlerEnvironment.message.chat.id, EXAM_CHANGED)
        sendMessage(textHandlerEnvironment, editStudentMessageCreator)
    }

    fun checkCurrentData(textHandlerEnvironment: TextHandlerEnvironment) {
        val chatId = textHandlerEnvironment.message.chat.id
        sendMessage(
            textHandlerEnvironment.bot,
            chatId,
            studentService.listStudents(chatId).joinToString("\n\n")
        )
        sendMessage(textHandlerEnvironment, editStudentMessageCreator)
    }

    fun sendTextMessage(messageText: String): HandleText {
        return {
            sendMessage(
                bot,
                message.chat.id,
                messageText
            )
        }
    }



    companion object {
        private const val EXAM_CHANGED = "Тип экзамена успешно изменен ✅"
        private const val YEAR_MESSAGE = "Укажите год сдачи экзамена: "
        private const val SERIES_MESSAGE = "Укажите серию паспорта: "
        private const val NUMBER_MESSAGE = "Укажите номер паспорта: "
        private const val SURNAME_MESSAGE = "Укажите фамилию: "
    }

}