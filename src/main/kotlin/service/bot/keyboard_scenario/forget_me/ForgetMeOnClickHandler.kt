package ege.bot.service.bot.keyboard_scenario.forget_me

import com.github.kotlintelegrambot.dispatcher.handlers.HandleText
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.entities.ReplyKeyboardRemove
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.OnClickHandler
import ege.bot.service.bot.keyboard_scenario.menu.MenuKeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService
import org.springframework.stereotype.Service

@Service
class ForgetMeOnClickHandler(
    private val chatService: ChatService,
    private val menuKeyboardMessageCreator: MenuKeyboardMessageCreator
) : OnClickHandler<ForgetMeKeyboardItem>(ForgetMeKeyboardItem::class.java, ChatState.FORGET_ME, chatService) {

    override fun getHandlerFunction(button: ForgetMeKeyboardItem): HandleText {
        return when (button) {
            ForgetMeKeyboardItem.I_AM_SURE -> this::deleteChat
            ForgetMeKeyboardItem.I_AM_NOT_SURE -> runMessageScenario(menuKeyboardMessageCreator)
        }
    }

    override fun getNextState(button: ForgetMeKeyboardItem, chatMode: ChatMode?, chatId: Long?): ChatState? {
        return when(button) {
            ForgetMeKeyboardItem.I_AM_SURE -> null
            ForgetMeKeyboardItem.I_AM_NOT_SURE -> ChatState.MENU
        }
    }

    private fun deleteChat(textHandlerEnvironment: TextHandlerEnvironment) {
        val chatId = textHandlerEnvironment.message.chat.id
        chatService.deleteChat(textHandlerEnvironment)
        sendMessage(textHandlerEnvironment.bot, chatId, SUCCESSFUL_DELETION, ReplyKeyboardRemove())
    }

    companion object {
        private const val SUCCESSFUL_DELETION = "Вся информацию об этом чате, учениках и их результатах удалена!"
    }
}