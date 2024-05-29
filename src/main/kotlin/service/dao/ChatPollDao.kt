package ege.bot.service.dao

import ege.bot.model.ChatPoll
import ege.bot.service.codegen.models.tables.references.CHAT_POLLS
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class ChatPollDao(
    private val dslContext: DSLContext
) {

    fun selectChatPoll(pollId: Long): ChatPoll? {
        return dslContext.select(
            CHAT_POLLS.CHAT_ID,
            CHAT_POLLS.POLL_ID,
            CHAT_POLLS.MESSAGE_ID
        )
            .from(CHAT_POLLS)
            .where(CHAT_POLLS.POLL_ID.eq(pollId))
            .fetchOneInto(ChatPoll::class.java)
    }

    fun deletePoll(pollId: Long) {
        dslContext.deleteFrom(CHAT_POLLS)
            .where(CHAT_POLLS.POLL_ID.eq(pollId))
            .execute()
    }

    fun insertPoll(chatPoll: ChatPoll) {
        dslContext.insertInto(CHAT_POLLS)
            .set(CHAT_POLLS.CHAT_ID, chatPoll.chatId)
            .set(CHAT_POLLS.POLL_ID, chatPoll.pollId)
            .set(CHAT_POLLS.MESSAGE_ID, chatPoll.messageId)
            .execute()
    }
}