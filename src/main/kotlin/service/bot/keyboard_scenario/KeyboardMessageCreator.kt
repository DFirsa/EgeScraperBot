package ege.bot.service.bot.keyboard_scenario

import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup

open class KeyboardMessageCreator<T> (
    private val text: String,
    private val enumClass: Class<T>
) where T: Enum<T>, T: KeyboardButtonItem {

    /**
     * Get keyboard markup
     */
    fun getKeyboard(chatId: Long): KeyboardReplyMarkup {
        val rows = enumClass.enumConstants.asList()
            .filter { !filterButton(it, chatId) }
            .map(KeyboardButtonItem::getButtonText)
            .map { listOf(it) }

        return KeyboardReplyMarkup.createSimpleKeyboard(
            rows, oneTimeKeyboard = true
        )
    }

    /**
     * Get message text
     */
    fun getMessage(): String {
        return text
    }

    protected open fun filterButton(button: T, chatId: Long): Boolean {
        return false
    }
}