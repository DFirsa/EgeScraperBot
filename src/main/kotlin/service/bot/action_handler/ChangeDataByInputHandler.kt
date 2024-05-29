package ege.bot.service.bot.action_handler

import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.model.ValidationException
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.edit_student.EditStudentKeyboard
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService

class ChangeDataByInputHandler(
    chatService: ChatService,
    private val editStudentsMessageCreator: KeyboardMessageCreator<EditStudentKeyboard>,
    state: ChatState,
    private val runAction: (TextHandlerEnvironment) -> ValidationException?
): UserActionHandler<TextHandlerEnvironment>(state, chatService, ChatState.EDIT_STUDENT_MENU) {
    override fun processAction(context: TextHandlerEnvironment) {
        val chatId = context.message.chat.id
        runAction.invoke(context)?.let {
            sendMessage(context.bot, chatId, it.description)
            sendMessage(context, editStudentsMessageCreator)
            return
        }

        sendMessage(context.bot, chatId, SUCCESS)
        sendMessage(context, editStudentsMessageCreator)
    }

    companion object {
        private const val SUCCESS = "Данные успешно обновлены ✅"
    }
}