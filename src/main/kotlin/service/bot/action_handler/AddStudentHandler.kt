package ege.bot.service.bot.action_handler

import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.keyboard_scenario.students.StudentsMessageCreator
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.StudentService
import org.springframework.stereotype.Service

@Service
class AddStudentHandler(
    private val chatService: ChatService,
    private val studentsMessageCreator: StudentsMessageCreator,
    private val studentService: StudentService
) : UserActionHandler<TextHandlerEnvironment>(ChatState.ADD_STUDENT, chatService, ChatState.STUDENTS_MENU) {
    override fun processAction(context: TextHandlerEnvironment) {
        val chatId = context.message.chat.id
        val chatMode = chatService.getChat(context)?.chatMode
        val textToProcess = textToValidate(context.text, chatMode!!)

        val message = studentService.convertAndAdd(textToProcess, chatId)
        sendMessage(context.bot, chatId, message)
        sendMessage(context, studentsMessageCreator)
    }

    private fun textToValidate(text: String, chatMode: ChatMode): List<String> {
        if (chatMode == ChatMode.STUDENT) {
            return listOf(text.lines().first())
        }

        return text.lines()
    }

}