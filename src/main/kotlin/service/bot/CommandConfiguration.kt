package ege.bot.service.bot_command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId

typealias CommandHandler = CommandHandlerEnvironment.() -> Unit

class CommandConfiguration {
}