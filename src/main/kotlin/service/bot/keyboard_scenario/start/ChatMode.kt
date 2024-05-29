package ege.bot.service.bot.keyboard_scenario.start

import ege.bot.service.bot.keyboard_scenario.KeyboardButtonItem

enum class ChatMode(val emoji: String): KeyboardButtonItem {
    STUDENT("\uD83D\uDC68\u200D\uD83C\uDF93"),
    TEACHER("\uD83D\uDC69\u200D\uD83C\uDFEB");

    override fun getButtonText(): String {
        return "$emoji $name"
    }
}