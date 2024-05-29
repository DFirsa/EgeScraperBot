package ege.bot.service.bot.keyboard_scenario.menu

import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.helper_service.ChatService
import org.springframework.stereotype.Service

@Service
class MenuKeyboardMessageCreator(
    private val chatService: ChatService,
) : KeyboardMessageCreator<MenuKeyboardItems>(MESSAGE_TEXT, MenuKeyboardItems::class.java) {

    override fun filterButton(button: MenuKeyboardItems, chatId: Long): Boolean {
        val chatMode = chatService.getChat(chatId)?.chatMode
        if (button.requiredForChatMode == null) {
            return false
        }
        return button.requiredForChatMode != chatMode
    }

    companion object {
        private const val MESSAGE_TEXT = "Выберите действие из меню:"
    }
}