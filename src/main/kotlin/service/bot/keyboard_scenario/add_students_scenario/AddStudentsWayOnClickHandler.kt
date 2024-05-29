package ege.bot.service.bot.keyboard_scenario.add_students_scenario

import com.github.kotlintelegrambot.dispatcher.handlers.HandleText
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.OnClickHandler
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.keyboard_scenario.students.StudentsKeyboard
import ege.bot.service.bot.utils.sendFile
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.StudentExcelService
import org.springframework.stereotype.Service

@Service
class AddStudentsWayOnClickHandler(
    chatService: ChatService,
    private val studentsMessageCreator: KeyboardMessageCreator<StudentsKeyboard>,
    private val studentExcelService: StudentExcelService
): OnClickHandler<AddStudentsWayKeyboard>(
    AddStudentsWayKeyboard::class.java,
    ChatState.CHOOSE_WAY_TO_ADD_STUDENTS,
    chatService
) {
    override fun getHandlerFunction(button: AddStudentsWayKeyboard): HandleText {
        return when (button) {
            AddStudentsWayKeyboard.EXCEL -> this::sendFileToChat
            AddStudentsWayKeyboard.TEXT -> this::sendTextMessage
            AddStudentsWayKeyboard.BACK -> runMessageScenario(studentsMessageCreator)
        }
    }

    override fun getNextState(button: AddStudentsWayKeyboard, chatMode: ChatMode?, chatId: Long?): ChatState? {
        return when(button) {
            AddStudentsWayKeyboard.EXCEL -> ChatState.LISTENING_FOR_NEW_STUDENTS_EXCEL
            AddStudentsWayKeyboard.TEXT -> ChatState.ADD_STUDENT
            AddStudentsWayKeyboard.BACK -> ChatState.STUDENTS_MENU
        }
    }

    private fun sendTextMessage(textHandlerEnvironment: TextHandlerEnvironment) {
        sendMessage(
            textHandlerEnvironment.bot,
            textHandlerEnvironment.message.chat.id,
            MESSAGE_TEXT.trimMargin()
        )
    }

    private fun sendFileToChat(textHandlerEnvironment: TextHandlerEnvironment) {
        sendMessage(
            textHandlerEnvironment.bot,
            textHandlerEnvironment.message.chat.id,
            EXCEL_TEXT.trimMargin()
        )
        sendFile(
            textHandlerEnvironment.bot,
            textHandlerEnvironment.message.chat.id,
            FILE_CAPTION,
            studentExcelService.createExample()
        )
    }

    companion object {
        private const val MESSAGE_TEXT = """
            |Укажите информацию об ученике в следующем формате:
            |<Экзамен> <год сдачи> <серия паспорта> <номер паспорта> <фамилия>
            |
            |Пример ЕГЭ: `EGE 2020 1234 123123 Иванов`
            |Пример ГИА: `OGE 2020 1234 123123 Петрова` 
            |
            |Информацию о каждом новом ученике необходимо вводить с новой строки!
        """

        private const val EXCEL_TEXT = "Заполните excel-файл данными об учениках по шаблону, приведенные в " +
                "качестве примера данные следует заменить. После заполнения пришлите этот файл"

        private const val FILE_CAPTION = "Шаблон файла для ввода данных"
    }
}