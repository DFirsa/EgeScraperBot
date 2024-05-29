package ege.bot.service.helper_service

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.PollAnswerHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.media.MediaHandlerEnvironment
import ege.bot.model.Chat
import ege.bot.model.ChatState
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.dao.ChatDao
import ege.bot.service.dao.ChatPollDao
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ChatService(
    private val chatDao: ChatDao,
    private val chatPollDao: ChatPollDao,
    private val pollDao: ChatPollDao
) {

    fun <T> getChat(context: T): Chat? {
        return chatDao.getChat(getChatId(context)).getOrNull()
    }

    fun <T> updateState(context: T, chatState: ChatState) {
        chatDao.updateState(chatState, getChatId(context))
        if (context is PollAnswerHandlerEnvironment) {
            pollDao.deletePoll(context.pollAnswer.pollId.toLong())
        }
    }

    fun <T> saveChat(context: T, chatMode: ChatMode) {
        chatDao.insert(
            Chat(
                getChatId(context),
                chatMode,
                ChatState.START
            )
        )
    }

    fun <T> deleteChat(context: T) {
        chatDao.delete(getChatId(context))
    }

    private fun <T> getChatId(context: T): Long {
        return when(context) {
            is CommandHandlerEnvironment -> context.message.chat.id
            is TextHandlerEnvironment -> context.message.chat.id
            is PollAnswerHandlerEnvironment -> chatPollDao.selectChatPoll(context.pollAnswer.pollId.toLong())!!.chatId
            is MediaHandlerEnvironment<*> -> context.message.chat.id
            is Long -> context
            else -> throw IllegalArgumentException("Illegal type for extracting chatId")
        }
    }
}