package ege.bot.service.bot.command

import ege.bot.model.Chat
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.menu.MenuKeyboardItems
import ege.bot.service.helper_service.ChatService
import org.springframework.stereotype.Service

@Service
class MenuCommandHandler(
    menuKeyboardMessageCreator: KeyboardMessageCreator<MenuKeyboardItems>,
    chatService: ChatService
) : AbstractCommandHandler<KeyboardMessageCreator<MenuKeyboardItems>>(
    menuKeyboardMessageCreator,
    COMMAND_NAME,
    INVALID_MESSAGE_TEXT,
    ChatState.MENU,
    chatService
) {
    override fun isValid(chat: Chat?): Boolean {
        return chat != null
    }

    companion object {
        private const val COMMAND_NAME = "menu"
        private const val INVALID_MESSAGE_TEXT = "Мы еще не знакомы, сначала выполните /start"
    }
}