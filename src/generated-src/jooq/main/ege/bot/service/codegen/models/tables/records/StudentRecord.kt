/*
 * This file is generated by jOOQ.
 */
package ege.bot.service.codegen.models.tables.records


import ege.bot.service.codegen.models.tables.Student

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * Students'es data
 */
@Suppress("UNCHECKED_CAST")
open class StudentRecord() : UpdatableRecordImpl<StudentRecord>(Student.STUDENT) {

    open var id: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open var chatId: Long?
        set(value): Unit = set(1, value)
        get(): Long? = get(1) as Long?

    open var passportSeries: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    open var passportNumber: String?
        set(value): Unit = set(3, value)
        get(): String? = get(3) as String?

    open var name: String?
        set(value): Unit = set(4, value)
        get(): String? = get(4) as String?

    open var examType: String?
        set(value): Unit = set(5, value)
        get(): String? = get(5) as String?

    open var examYear: Short?
        set(value): Unit = set(6, value)
        get(): Short? = get(6) as Short?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    /**
     * Create a detached, initialised StudentRecord
     */
    constructor(id: Long? = null, chatId: Long? = null, passportSeries: String? = null, passportNumber: String? = null, name: String? = null, examType: String? = null, examYear: Short? = null): this() {
        this.id = id
        this.chatId = chatId
        this.passportSeries = passportSeries
        this.passportNumber = passportNumber
        this.name = name
        this.examType = examType
        this.examYear = examYear
        resetChangedOnNotNull()
    }
}
