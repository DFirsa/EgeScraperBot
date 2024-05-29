package ege.bot.service.bot.action_handler

import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import ege.bot.model.ChatState
import ege.bot.model.ValidationException
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import ege.bot.service.bot.keyboard_scenario.edit_student.EditStudentKeyboard
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.StudentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EditActionHandlerConfiguration(
    private val chatService: ChatService,
    private val editStudentsMessageCreator: KeyboardMessageCreator<EditStudentKeyboard>,
    private val studentService: StudentService
) {

    @Bean
    fun changeExamYearHandler(): UserActionHandler<TextHandlerEnvironment> {
        return buildHandler(ChatState.EDIT_YEAR, studentService::updateExamYear)
    }

    @Bean
    fun seriesHandler(): UserActionHandler<TextHandlerEnvironment> {
        return buildHandler(ChatState.EDIT_PASSPORT_SERIES) { studentService.updateSeriesOrNumber(it, true) }
    }

    @Bean
    fun numberHandler(): UserActionHandler<TextHandlerEnvironment> {
        return buildHandler(ChatState.EDIT_PASSPORT_NUMBER) { studentService.updateSeriesOrNumber(it, false) }
    }

    @Bean
    fun surnameHandler(): UserActionHandler<TextHandlerEnvironment> {
        return buildHandler(ChatState.EDIT_SURNAME, studentService::updateSurname)
    }

    private fun buildHandler(
        state: ChatState,
        action: (TextHandlerEnvironment) -> ValidationException?
    ): UserActionHandler<TextHandlerEnvironment> {
        return ChangeDataByInputHandler(
            chatService,
            editStudentsMessageCreator,
            state,
            action
        )
    }
}