package ege.bot.service.bot.utils

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.PollAnswerHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.media.MediaHandlerEnvironment
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.polls.PollType
import ege.bot.service.bot.keyboard_scenario.KeyboardButtonItem
import ege.bot.service.bot.keyboard_scenario.KeyboardMessageCreator
import java.io.File
import java.time.LocalDateTime
import javax.print.attribute.standard.Media

fun sendMessage(
    bot: Bot,
    chatId: Long,
    text: String,
    markup: ReplyMarkup? = null,
    messageIdForReply: Long? = null
) {
    bot.sendMessage(
        chatId = ChatId.fromId(chatId),
        parseMode = ParseMode.MARKDOWN,
        text = text,
        replyMarkup = markup,
        replyToMessageId = messageIdForReply
    )
}

fun <T> sendMessage(
    textHandlerEnvironment: TextHandlerEnvironment,
    keyboardMessageCreator: KeyboardMessageCreator<T>
) where T: Enum<T>, T: KeyboardButtonItem {
    val chatId = textHandlerEnvironment.message.chat.id
    sendMessage(
        textHandlerEnvironment.bot,
        chatId,
        keyboardMessageCreator.getMessage(),
        keyboardMessageCreator.getKeyboard(chatId)
    )
}

fun <T> sendMessage(
    pollAnswerHandlerEnvironment: PollAnswerHandlerEnvironment,
    chatId: Long,
    keyboardMessageCreator: KeyboardMessageCreator<T>
) where T: Enum<T>, T: KeyboardButtonItem {
    sendMessage(
        pollAnswerHandlerEnvironment.bot,
        chatId,
        keyboardMessageCreator.getMessage(),
        keyboardMessageCreator.getKeyboard(chatId)
    )
}

fun sendPoll(
    textHandlerEnvironment: TextHandlerEnvironment,
    options: List<String>,
    question: String
): Message? {
    val chatId = textHandlerEnvironment.message.chat.id
    return textHandlerEnvironment.bot.sendPoll(
        ChatId.fromId(chatId),
        question,
        options,
        isAnonymous = false,
        type = PollType.REGULAR,
        allowsMultipleAnswers = true
    ).getOrNull()
}

fun sendFile(
    bot: Bot,
    chatId: Long,
    text: String? = null,
    file: ByteArray
) {
    bot.sendDocument(
        chatId = ChatId.fromId(chatId),
        document = TelegramFile.ByByteArray(file, LocalDateTime.now().hashCode().toString() + ".xlsx"),
        caption = text,
        parseMode = ParseMode.MARKDOWN
    )
}