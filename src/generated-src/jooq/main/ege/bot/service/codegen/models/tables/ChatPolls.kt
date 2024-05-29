/*
 * This file is generated by jOOQ.
 */
package ege.bot.service.codegen.models.tables


import ege.bot.service.codegen.models.Results
import ege.bot.service.codegen.models.keys.CHAT_POLLS_PKEY
import ege.bot.service.codegen.models.keys.CHAT_POLLS__FK_CHAT_POLL_CHAT_ID
import ege.bot.service.codegen.models.tables.records.ChatPollsRecord

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * Information about active polls
 */
@Suppress("UNCHECKED_CAST")
open class ChatPolls(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, ChatPollsRecord>?,
    aliased: Table<ChatPollsRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<ChatPollsRecord>(
    alias,
    Results.RESULTS,
    child,
    path,
    aliased,
    parameters,
    DSL.comment("Information about active polls"),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>results.chat_polls</code>
         */
        val CHAT_POLLS: ChatPolls = ChatPolls()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<ChatPollsRecord> = ChatPollsRecord::class.java

    /**
     * The column <code>results.chat_polls.poll_id</code>. Poll id
     */
    val POLL_ID: TableField<ChatPollsRecord, Long?> = createField(DSL.name("poll_id"), SQLDataType.BIGINT.nullable(false), this, "Poll id")

    /**
     * The column <code>results.chat_polls.chat_id</code>. Chat with poll
     */
    val CHAT_ID: TableField<ChatPollsRecord, Long?> = createField(DSL.name("chat_id"), SQLDataType.BIGINT.nullable(false), this, "Chat with poll")

    /**
     * The column <code>results.chat_polls.message_id</code>. Message id with
     * specific poll
     */
    val MESSAGE_ID: TableField<ChatPollsRecord, Long?> = createField(DSL.name("message_id"), SQLDataType.BIGINT.nullable(false), this, "Message id with specific poll")

    private constructor(alias: Name, aliased: Table<ChatPollsRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<ChatPollsRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>results.chat_polls</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>results.chat_polls</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>results.chat_polls</code> table reference
     */
    constructor(): this(DSL.name("chat_polls"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, ChatPollsRecord>): this(Internal.createPathAlias(child, key), child, key, CHAT_POLLS, null)
    override fun getSchema(): Schema? = if (aliased()) null else Results.RESULTS
    override fun getPrimaryKey(): UniqueKey<ChatPollsRecord> = CHAT_POLLS_PKEY
    override fun getReferences(): List<ForeignKey<ChatPollsRecord, *>> = listOf(CHAT_POLLS__FK_CHAT_POLL_CHAT_ID)

    private lateinit var _chat: Chat

    /**
     * Get the implicit join path to the <code>results.chat</code> table.
     */
    fun chat(): Chat {
        if (!this::_chat.isInitialized)
            _chat = Chat(this, CHAT_POLLS__FK_CHAT_POLL_CHAT_ID)

        return _chat;
    }

    val chat: Chat
        get(): Chat = chat()
    override fun `as`(alias: String): ChatPolls = ChatPolls(DSL.name(alias), this)
    override fun `as`(alias: Name): ChatPolls = ChatPolls(alias, this)
    override fun `as`(alias: Table<*>): ChatPolls = ChatPolls(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): ChatPolls = ChatPolls(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): ChatPolls = ChatPolls(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): ChatPolls = ChatPolls(name.getQualifiedName(), null)
}
