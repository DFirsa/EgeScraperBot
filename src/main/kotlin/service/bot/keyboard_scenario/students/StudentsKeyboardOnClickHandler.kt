package ege.bot.service.bot.keyboard_scenario.students

import com.github.kotlintelegrambot.dispatcher.handlers.HandleText
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatPoll
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.OnClickHandler
import ege.bot.service.bot.keyboard_scenario.add_students_scenario.AddStudentsWayKeyboard
import ege.bot.service.bot.keyboard_scenario.edit_student.EditStudentKeyboard
import ege.bot.service.bot.keyboard_scenario.menu.MenuKeyboardItems
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.utils.sendFile
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.bot.utils.sendPoll
import ege.bot.service.dao.ChatPollDao
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.StudentExcelService
import ege.bot.service.helper_service.StudentService
import org.springframework.stereotype.Service

@Service
class StudentsKeyboardOnClickHandler(
    private val menuMessageCreator: KeyboardMessageCreator<MenuKeyboardItems>,
    private val chatService: ChatService,
    private val studentService: StudentService,
    private val studentMessageCreator: KeyboardMessageCreator<StudentsKeyboard>,
    private val editStudentMessageCreator: KeyboardMessageCreator<EditStudentKeyboard>,
    private val chatPollDao: ChatPollDao,
    private val addStudentsOptionMessageCreator: KeyboardMessageCreator<AddStudentsWayKeyboard>,
    private val studentExcelService: StudentExcelService
) : OnClickHandler<StudentsKeyboard>(StudentsKeyboard::class.java, ChatState.STUDENTS_MENU, chatService) {
    override fun getHandlerFunction(button: StudentsKeyboard): HandleText {
        return when (button) {
            StudentsKeyboard.ADD_STUDENTS -> this::addStudents
            StudentsKeyboard.EDIT_INFORMATION -> this::runEditMessageScenario
            StudentsKeyboard.DELETE_STUDENTS -> this::deleteStudents
            StudentsKeyboard.TO_MENU -> runMessageScenario(menuMessageCreator)
            StudentsKeyboard.STUDENTS_DATA -> this::printStudentsScenario
        }
    }

    override fun getNextState(button: StudentsKeyboard, chatMode: ChatMode?, chatId: Long?): ChatState? {
        return when (button) {
            StudentsKeyboard.EDIT_INFORMATION -> resolveEditNextState(chatMode)
            StudentsKeyboard.DELETE_STUDENTS -> resolveDeleteNextState(chatMode, chatId)
            StudentsKeyboard.ADD_STUDENTS -> resolveAddStudentNextState(chatMode)
            StudentsKeyboard.TO_MENU -> ChatState.MENU
            StudentsKeyboard.STUDENTS_DATA -> ChatState.STUDENTS_MENU
        }
    }

    private fun resolveAddStudentNextState(chatMode: ChatMode?): ChatState {
        return  if (chatMode == ChatMode.STUDENT) ChatState.ADD_STUDENT
        else ChatState.CHOOSE_WAY_TO_ADD_STUDENTS
    }

    private fun resolveDeleteNextState(chatMode: ChatMode?, chatId: Long?): ChatState {
        if (chatMode == ChatMode.STUDENT) {
            return ChatState.STUDENTS_MENU
        }

        if (studentService.listStudents(chatId!!).size > 1) {
            return ChatState.CHOOSE_STUDENT_FOR_DELETION
        }

        return ChatState.STUDENTS_MENU
    }

    private fun resolveEditNextState(chatMode: ChatMode?): ChatState {
        return  if (chatMode == ChatMode.STUDENT) ChatState.EDIT_STUDENT_MENU
                else ChatState.LISTENING_FOR_UPDATED_STUDENTS
    }

    private fun printStudentsScenario(textHandlerEnvironment: TextHandlerEnvironment) {
        val chatId = textHandlerEnvironment.message.chat.id
        val students = studentService.listStudents(chatId)
            .sortedWith(compareBy(
                { it.examType.name },
                { it.examYear },
                { it.surname }
            ))
            .joinToString("\n") { it.getStudentString() }
        sendMessage(textHandlerEnvironment.bot, chatId, "Данные о ваших учениках:\n\n$students")
        sendMessage(textHandlerEnvironment, studentMessageCreator)
    }

    private fun runEditMessageScenario(textHandlerEnvironment: TextHandlerEnvironment) {
        wrap(
            textHandlerEnvironment,
            { sendMessage(it, editStudentMessageCreator) },
            {
                val chatId = textHandlerEnvironment.message.chat.id
                sendMessage(
                    textHandlerEnvironment.bot,
                    chatId,
                    EDIT_STUDENT_TEXT
                )
                sendFile(
                    textHandlerEnvironment.bot,
                    chatId,
                    YOUR_STUDENTS,
                    studentExcelService.buildStudentsFile(chatId)
                )
            }
        )
    }

    private fun addStudents(textHandlerEnvironment: TextHandlerEnvironment) {
        wrap(
            textHandlerEnvironment,
            { sendMessage(it.bot, it.message.chat.id, STUDENT_ADD_TEXT_SUGGEST.trimMargin()) },
            { sendMessage(it, addStudentsOptionMessageCreator) }
        )
    }

    private fun deleteStudents(textHandlerEnvironment: TextHandlerEnvironment) {
        wrap(
            textHandlerEnvironment,
            {
                studentService.deleteAll(it.message.chat.id)
                sendMessage(it.bot, it.message.chat.id, SUCCESSFULLY_DELETED)
                sendMessage(textHandlerEnvironment, studentMessageCreator)
            },
            {
                val options = studentService.listStudents(it.message.chat.id)
                    .sortedBy { student -> student.id }
                    .map { student -> student.getStudentString() }

                if (options.size == 1) {

                    studentService.deleteAll(textHandlerEnvironment.message.chat.id)
                    sendMessage(
                        textHandlerEnvironment.bot,
                        textHandlerEnvironment.message.chat.id,
                        SUCCESSFULLY_DELETED
                    )
                    sendMessage(textHandlerEnvironment, studentMessageCreator)

                } else {

                    val message = sendPoll(it, options, CHOOSE_STUDENTS_TO_DELETE)!!
                    chatPollDao.insertPoll(
                        ChatPoll(
                            message.poll!!.id,
                            message.chat.id,
                            message.messageId
                        )
                    )
                }
            }
        )

    }

    private fun wrap(
        textHandlerEnvironment: TextHandlerEnvironment,
        studentAction: (TextHandlerEnvironment) -> Unit,
        teacherAction: (TextHandlerEnvironment) -> Unit
    ) {
        val chatMode = chatService.getChat(textHandlerEnvironment)?.chatMode

        if (chatMode == ChatMode.STUDENT) {
            studentAction.invoke(textHandlerEnvironment)
            return
        }

        teacherAction.invoke(textHandlerEnvironment)
    }

    companion object {
        private const val STUDENT_ADD_TEXT_SUGGEST = """
            |Укажите информацию об ученике в следующем формате:
            |<Экзамен> <год сдачи> <серия паспорта> <номер паспорта> <фамилия>
            |
            |Пример ЕГЭ: `EGE 2020 1234 123123 Иванов`
            |Пример ГИА: `OGE 2020 1234 123123 Петрова`
        """

        private const val SUCCESSFULLY_DELETED = "Данные успешно удалены ✅"
        private const val CHOOSE_STUDENTS_TO_DELETE = "Выберите студентов для удаления"
        private const val EDIT_STUDENT_TEXT = "Измените информацию об учениках, которая указана некорректно. После этого пришлите excel-файл"
        private const val YOUR_STUDENTS = "Ваши ученики:"
    }
}