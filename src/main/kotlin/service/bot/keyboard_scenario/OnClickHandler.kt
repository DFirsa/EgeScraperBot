package ege.bot.service.bot.keyboard_scenario

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.HandleText
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService

abstract class OnClickHandler<T>(
    private val enumClass: Class<T>,
    private val validState: ChatState? = null,
    private val chatService: ChatService
) where T : Enum<T>, T : KeyboardButtonItem {

    fun initHandlers(dispatcher: Dispatcher) {
        enumClass.enumConstants
            .toList()
            .forEach {
                dispatcher.text(it.getButtonText()) {
                    val chat = chatService.getChat(this)
                    if (chat?.state == validState) {
                        getHandlerFunction(it).invoke(this)

                        update.consume()
                        val nextState = getNextState(it, chat?.chatMode, chat?.id)
                        if (nextState != null) {
                            chatService.updateState(this.message.chat.id, nextState)
                        }
                    }
                }
            }
    }

    protected abstract fun getHandlerFunction(button: T): HandleText

    protected abstract fun getNextState(button: T, chatMode: ChatMode?, chatId: Long?): ChatState?

    fun <R> runMessageScenario(messageCreator: KeyboardMessageCreator<R>): HandleText
            where R : Enum<R>, R : KeyboardButtonItem {
        return {
            sendMessage(this, messageCreator)
        }
    }

    fun doNothing(textHandlerEnvironment: TextHandlerEnvironment) {
        //
    }
}