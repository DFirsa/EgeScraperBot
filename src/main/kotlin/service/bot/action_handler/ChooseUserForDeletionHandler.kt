package ege.bot.service.bot.action_handler

import com.github.kotlintelegrambot.dispatcher.handlers.PollAnswerHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.students.StudentsMessageCreator
import ege.bot.service.bot.utils.sendMessage
import ege.bot.service.dao.ChatPollDao
import ege.bot.service.helper_service.ChatService
import ege.bot.service.helper_service.StudentService
import org.springframework.stereotype.Service

@Service
class ChooseUserForDeletionHandler(
    chatService: ChatService,
    private val studentService: StudentService,
    private val chatPollDao: ChatPollDao,
    private val studentsMessageCreator: StudentsMessageCreator
): UserActionHandler<PollAnswerHandlerEnvironment>(
    ChatState.CHOOSE_STUDENT_FOR_DELETION,
    chatService,
    ChatState.STUDENTS_MENU
) {
    override fun processAction(context: PollAnswerHandlerEnvironment) {
        val chatPoll = chatPollDao.selectChatPoll(context.pollAnswer.pollId.toLong()) ?: return
        context.bot.stopPoll(ChatId.fromId(chatPoll.chatId), chatPoll.messageId)
        val orders = context.pollAnswer.optionIds
        studentService.deleteByOrders(orders, chatPoll.chatId)
        sendMessage(
            context.bot,
            chatPoll.chatId,
            SUCCESSFULLY_DELETED
        )
//        chatPollDao.deletePoll(chatPoll.pollId)
        sendMessage(context, chatPoll.chatId, studentsMessageCreator)
    }

    companion object {
        private const val SUCCESSFULLY_DELETED = "Указанные ученики успешно удалены ✅"
    }
}