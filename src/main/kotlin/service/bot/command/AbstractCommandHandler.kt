package ege.bot.service.bot.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import ege.bot.model.Chat
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService

abstract class AbstractCommandHandler<T : KeyboardMessageCreator<*>>(
    private val messageCreator: KeyboardMessageCreator<*>,
    private val commandName: String,
    private val invalidMessageText: String,
    private val newState: ChatState? = null,
    private val chatService: ChatService
) {
    fun init(dispatcher: Dispatcher) {
        dispatcher.command(commandName) {
            val chatId = message.chat.id

            if (isValid(chatService.getChat(this))) {
                if (newState != null) {
                    chatService.updateState(this.message.chat.id, newState)
                }
                sendMessage(bot, chatId, messageCreator.getMessage(), messageCreator.getKeyboard(chatId))
                return@command
            }
            sendMessage(bot, chatId, invalidMessageText, messageIdForReply = message.messageId)
        }
    }

    abstract fun isValid(chat: Chat?): Boolean
}