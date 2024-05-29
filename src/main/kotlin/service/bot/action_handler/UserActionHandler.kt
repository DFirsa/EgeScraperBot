package ege.bot.service.bot.action_handler

import com.github.kotlintelegrambot.dispatcher.handlers.PollAnswerHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.service.helper_service.ChatService

abstract class UserActionHandler<T>(
    private val state: ChatState,
    private val chatService: ChatService,
    private val nextState: ChatState
) {
    fun handleAction(context: T) {
        if (chatService.getChat(context)?.state != state) {
            return
        }

        consumeUpdate(context)
        processAction(context)
        chatService.updateState(context, nextState)
    }

    /**
     * returns chatId
     */
    protected abstract fun processAction(context: T)

    private fun consumeUpdate(context: T) {
        when (context) {
            is TextHandlerEnvironment -> context.update.consume()
            is PollAnswerHandlerEnvironment -> context.update.consume()
        }
    }
}