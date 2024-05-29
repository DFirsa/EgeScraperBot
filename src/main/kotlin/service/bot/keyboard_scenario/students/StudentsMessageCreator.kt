package ege.bot.service.bot.keyboard_scenario.students

import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.dao.StudentDao
import ege.bot.service.helper_service.ChatService
import org.springframework.stereotype.Service

@Service
class StudentsMessageCreator(
    private val chatService: ChatService,
    private val studentDao: StudentDao
) : KeyboardMessageCreator<StudentsKeyboard>(MESSAGE, StudentsKeyboard::class.java) {

    override fun filterButton(button: StudentsKeyboard, chatId: Long): Boolean {
        val chatMode = chatService.getChat(chatId)?.chatMode
        val hasStudents = studentDao.hasStudents(chatId)
        return when (button) {
            StudentsKeyboard.EDIT_INFORMATION -> !hasStudents
            StudentsKeyboard.DELETE_STUDENTS -> !hasStudents
            StudentsKeyboard.ADD_STUDENTS -> hasStudents && chatMode == ChatMode.STUDENT
            StudentsKeyboard.TO_MENU -> false
            StudentsKeyboard.STUDENTS_DATA -> chatMode != ChatMode.TEACHER || !hasStudents
        }
    }

    companion object {
        private const val MESSAGE = "Выберите действие для работы с учениками:"
    }
}