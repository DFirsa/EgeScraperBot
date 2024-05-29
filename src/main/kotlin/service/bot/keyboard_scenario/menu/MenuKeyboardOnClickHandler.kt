package ege.bot.service.bot.keyboard_scenario.menu

import com.github.kotlintelegrambot.dispatcher.handlers.HandleText
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.OnClickHandler
import ege.bot.service.bot.keyboard_scenario.forget_me.ForgetMeKeyboardItem
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.keyboard_scenario.students.StudentsKeyboard
import ege.bot.service.bot.keyboard_scenario.teacher_print_result.PrintResultKeyboard
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.ResultsMessageCreatingService
import org.springframework.stereotype.Service

@Service
class MenuKeyboardOnClickHandler(
    private val forgetMeKeyboardMessageCreator: KeyboardMessageCreator<ForgetMeKeyboardItem>,
    private val studentsMessageCreator: KeyboardMessageCreator<StudentsKeyboard>,
    private val chatService: ChatService,
    private val resultsMessageCreatingService: ResultsMessageCreatingService,
    private val menuKeyboardMessageCreator: MenuKeyboardMessageCreator,
    private val printResultMessageCreator: KeyboardMessageCreator<PrintResultKeyboard>
) : OnClickHandler<MenuKeyboardItems>(MenuKeyboardItems::class.java, ChatState.MENU, chatService) {
    override fun getHandlerFunction(button: MenuKeyboardItems): HandleText {
        return when (button) {
            MenuKeyboardItems.FORGET_ME -> runMessageScenario(forgetMeKeyboardMessageCreator)
            MenuKeyboardItems.CHECK_RESULTS -> this::showResults
            MenuKeyboardItems.STUDENT -> runMessageScenario(studentsMessageCreator)
            MenuKeyboardItems.STUDENTS -> runMessageScenario(studentsMessageCreator)
        }
    }

    override fun getNextState(button: MenuKeyboardItems, chatMode: ChatMode?, chatId: Long?): ChatState? {
        return when(button) {
            MenuKeyboardItems.CHECK_RESULTS -> if (chatMode == ChatMode.STUDENT) ChatState.MENU else ChatState.PRINT_RESULT_MENU
            MenuKeyboardItems.STUDENT -> ChatState.STUDENTS_MENU
            MenuKeyboardItems.STUDENTS -> ChatState.STUDENTS_MENU
            MenuKeyboardItems.FORGET_ME -> ChatState.FORGET_ME
        }
    }

    private fun showResults(textHandlerEnvironment: TextHandlerEnvironment) {
        val chatId = textHandlerEnvironment.message.chat.id

        if (chatService.getChat(chatId)!!.chatMode == ChatMode.STUDENT) {
            val text = resultsMessageCreatingService.createTextMessage(chatId)
            sendMessage(textHandlerEnvironment.bot, chatId, text)
            sendMessage(textHandlerEnvironment, menuKeyboardMessageCreator)
            return
        }

        sendMessage(textHandlerEnvironment, printResultMessageCreator)
    }
}