package ege.bot.service.bot_command

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextStartedEvent
import org.springframework.context.event.ContextStoppedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class TelegramBotService(
    @Value("\${tg-bot.token}")
    private val botToken: String
) {

    private val bot = bot {
        token = botToken
        dispatch {
            command("start") {
                answerUsingText(message, START_TEXT)
            }
            command("pass-data") {
                // TODO insert data
            }
            command("see-result") {
                // TODO query to the database
            }
            command("edit") {
                // TODO update query
            }
            command("forget") {
                // TODO remove passport data from database
            }
        }
    }

    init {
        bot.startPolling()
    }

    /**
     * Write to user text using id of the chat
     */
    fun text(chatId: Long, text: String) {
        bot.sendMessage(chatId = ChatId.fromId(chatId), text = text)
    }

    /**
     * Answer for command request
     */
    private fun answerUsingText(message: Message, text: String) {
        text(message.chat.id, text)
    }

    companion object {
        const val START_TEXT = """
            Привет!
            Я бот для автоматизации получения результатов ЕГЭ/ОГЭ. Ну или вкратце - твоего удобства.
            
            Как пользоваться:
            1. Передай мне твои паспортные данные
            2. Если хочешь, можешь указать псевдоним для паспортных данных
            3. Когда я получу обновления по твоим результатам - пришлю их тебе.
            4. Если захочешь удалить твои паспортные данные - всегда можешь это сделать, но в таком случае я не смогу обновлять результаты твоих экзаменов.
            
            P.S. Если ты опасаешься за сохранность своих приватных данных - весь код бота полностью открытый. Можешь его посмотреть самостоятельно: 
            И не забудь поставить звездочку (я буду знать что делал бота не зря)!
        """

    }
}