package ege.bot.service.bot.keyboard_scenario.teacher_print_result

import com.github.kotlintelegrambot.dispatcher.handlers.HandleText
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.OnClickHandler
import ege.bot.service.bot.keyboard_scenario.menu.MenuKeyboardItems
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.utils.sendFile
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.ResultExcelService
import ege.bot.service.helper_service.ResultsMessageCreatingService
import org.springframework.stereotype.Service

@Service
class PrintResultOnClickHandler(
    chatService: ChatService,
    private val menuMessageCreator: KeyboardMessageCreator<MenuKeyboardItems>,
    private val resultExcelService: ResultExcelService,
    private val resultsMessageCreatingService: ResultsMessageCreatingService
): OnClickHandler<PrintResultKeyboard>(
    PrintResultKeyboard::class.java,
    ChatState.PRINT_RESULT_MENU,
    chatService
) {
    override fun getHandlerFunction(button: PrintResultKeyboard): HandleText {
        return when(button) {
            PrintResultKeyboard.EXCEL_FILE -> this::sendResultFile
            PrintResultKeyboard.TEXT_IN_CHAT -> this::sendTextResult
        }
    }

    override fun getNextState(button: PrintResultKeyboard, chatMode: ChatMode?, chatId: Long?): ChatState? {
        return ChatState.MENU
    }

    private fun sendResultFile(textHandlerEnvironment: TextHandlerEnvironment) {
        val chatId = textHandlerEnvironment.message.chat.id
        val excel = resultExcelService.createResultExcelFile(chatId) ?: run {
            sendMessage(
                textHandlerEnvironment.bot,
                chatId,
                NOT_FOUND
            )
            sendMessage(textHandlerEnvironment, menuMessageCreator)
            return
        }

        sendFile(textHandlerEnvironment.bot, chatId, RESULTS, excel)
        sendMessage(textHandlerEnvironment, menuMessageCreator)
    }

    private fun sendTextResult(textHandlerEnvironment: TextHandlerEnvironment) {
        val chatId = textHandlerEnvironment.message.chat.id
        sendMessage(textHandlerEnvironment.bot,
            chatId,
            resultsMessageCreatingService.createTextMessage(chatId)
        )
        sendMessage(textHandlerEnvironment, menuMessageCreator)
    }

    companion object {
        private const val RESULTS = "Результаты ваших учеников :"
        private const val NOT_FOUND = "Результатов еще нет или ученики не указаны \uD83E\uDD37\u200D♂\uFE0F"
    }
}