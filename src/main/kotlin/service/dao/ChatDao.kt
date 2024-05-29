package ege.bot.service.dao

import ege.bot.model.Chat
import ege.bot.model.ChatState
import ege.bot.service.codegen.models.tables.references.CHAT
import org.jooq.DSLContext
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ChatDao(
    val dslContext: DSLContext
) {
    fun insert(chat: Chat) {
        dslContext.insertInto(CHAT)
            .set(CHAT.ID, chat.id)
            .set(CHAT.CHAT_MODE, chat.chatMode.name)
            .set(CHAT.STATE, chat.state.name)
            .onConflictDoNothing()
            .execute()
    }

    fun getChat(id: Long): Optional<Chat> {
        return dslContext.select()
            .from(CHAT)
            .where(CHAT.ID.eq(id))
            .fetchOneInto(Chat::class.java)
            .let { Optional.ofNullable(it) }
    }

    fun delete(id: Long) {
        dslContext.deleteFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .execute()
    }

    fun updateState(state: ChatState, chatId: Long) {
        dslContext.update(CHAT)
            .set(CHAT.STATE, state.toString())
            .where(CHAT.ID.eq(chatId))
            .execute()
    }
}