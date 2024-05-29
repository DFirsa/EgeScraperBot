package ege.bot.service.bot.command

import ege.bot.model.Chat
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.helper_service.ChatService
import org.springframework.stereotype.Service

@Service
class StartCommandHandler(
    startKeyboardMessageCreator: KeyboardMessageCreator<ChatMode>,
    chatService: ChatService
) : AbstractCommandHandler<KeyboardMessageCreator<ChatMode>>(
    startKeyboardMessageCreator,
    COMMAND_NAME,
    INVALID_MESSAGE_TEXT,
    chatService = chatService
) {

    override fun isValid(chat: Chat?): Boolean {
        return chat == null
    }

    companion object {
        private const val COMMAND_NAME = "start"
        private const val INVALID_MESSAGE_TEXT = "Мы уже здоровались :)"
    }
}