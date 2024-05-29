package ege.bot.service.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.document
import com.github.kotlintelegrambot.dispatcher.handlers.PollAnswerHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.media.MediaHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.pollAnswer
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.files.Document
import ege.bot.service.bot.action_handler.UserActionHandler
import ege.bot.service.bot.command.AbstractCommandHandler
import ege.bot.service.bot.keyboard_scenario.OnClickHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TelegramBotService(
    @Value("\${tg-bot.token}")
    private val botToken: String,
    private val commands: List<AbstractCommandHandler<*>>,
    private val clickHandlers: List<OnClickHandler<*>>,
    private val textHandlers: List<UserActionHandler<TextHandlerEnvironment>>,
    private val pollHandlers: List<UserActionHandler<PollAnswerHandlerEnvironment>>,
    private val fileHandlers: List<UserActionHandler<MediaHandlerEnvironment<Document>>>
) {

    private val bot = bot {
        token = botToken
        dispatch {
            clickHandlers.forEach { it.initHandlers(this) }
            commands.forEach { it.init(this) }
            text {
                textHandlers.forEach { it.handleAction(this) }
            }
            pollAnswer {
                pollHandlers.forEach { it.handleAction(this) }
            }
            document {
                fileHandlers.forEach { it.handleAction(this) }
            }
        }
    }

    init {
        bot.startPolling()
    }

    /**
     * Write to user text using id of the chat
     */
    fun send(chatId: Long, text: String, markup: ReplyMarkup?) {
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = text,
            replyMarkup = markup
        )

    }

    /**
     * Send file
     */
    fun send(chatId: Long, text: String, file: ByteArray) {
        bot.sendDocument(
            chatId = ChatId.fromId(chatId),
            document = TelegramFile.ByByteArray(file, LocalDateTime.now().hashCode().toString() + ".xlsx"),
            caption = text,
            parseMode = ParseMode.MARKDOWN
        )
    }
}