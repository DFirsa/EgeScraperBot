package ege.bot.service.bot.keyboard_scenario.start

import com.github.kotlintelegrambot.dispatcher.handlers.HandleText
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.OnClickHandler
import ege.bot.service.bot.keyboard_scenario.menu.MenuKeyboardMessageCreator
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService
import org.springframework.stereotype.Service

@Service
class StartKeyboardOnClickHandler(
    private val chatService: ChatService,
    private val menuKeyboardMessageCreator: MenuKeyboardMessageCreator
) : OnClickHandler<ChatMode>(ChatMode::class.java, chatService = chatService) {

    override fun getHandlerFunction(button: ChatMode): HandleText {
        return { storeMode(button, this) }
    }

    override fun getNextState(button: ChatMode, chatMode: ChatMode?, chatId: Long?): ChatState? {
        return ChatState.MENU
    }

    private fun storeMode(chatMode: ChatMode, textHandlerEnvironment: TextHandlerEnvironment) {
        chatService.saveChat(textHandlerEnvironment, chatMode)
        sendMessage(textHandlerEnvironment, menuKeyboardMessageCreator)
    }
}