package ege.bot.model

import ege.bot.service.bot.keyboard_scenario.start.ChatMode

data class Chat(
    val id: Long,
    val chatMode: ChatMode,
    val state: ChatState
)
