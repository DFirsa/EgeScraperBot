package ege.bot.service.dao

import ege.bot.model.Student
import ege.bot.service.codegen.models.tables.references.STUDENT
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Service
import java.time.Year

@Service
class StudentDao(
    val dslContext: DSLContext
) {
    fun hasStudents(chatId: Long): Boolean {
        return !dslContext.select(STUDENT.ID)
            .from(STUDENT)
            .where(STUDENT.CHAT_ID.eq(chatId))
            .fetch()
            .isEmpty()
    }

    fun selectStudentsByYear(year: Year): List<Student> {
        return dslContext.select()
            .from(STUDENT)
            .where(STUDENT.EXAM_YEAR.eq(year.value.toShort()))
            .fetchInto(Student::class.java)
    }

    fun selectStudentsByOrder(orders: List<Int>, chatId: Long): List<Student> {
        val rowNumberField = rowNumber().over(orderBy(STUDENT.ID)).`as`("rn")
        val cte = name("data").`as`(
            select(asterisk(), rowNumberField)
                .from(STUDENT)
                .where(STUDENT.CHAT_ID.eq(chatId))
        )

        return dslContext.with(cte).select(
            cte.field(STUDENT.ID.name),
            cte.field(STUDENT.PASSPORT_NUMBER.name),
            cte.field(STUDENT.PASSPORT_SERIES.name),
            cte.field(STUDENT.CHAT_ID.name),
            cte.field(STUDENT.EXAM_TYPE.name),
            cte.field(STUDENT.SURNAME.name),
            cte.field(STUDENT.EXAM_YEAR.name)
        )
            .from(cte)
            .where(rowNumberField.`in`(orders.map { it + 1 }))
            .fetchInto(Student::class.java)
    }

    fun insertStudents(students: List<Student>) {
        if (students.isEmpty()) {
            return
        }

        dslContext.insertInto(STUDENT)
            .columns(
                STUDENT.CHAT_ID,
                STUDENT.EXAM_YEAR,
                STUDENT.EXAM_TYPE,
                STUDENT.PASSPORT_SERIES,
                STUDENT.PASSPORT_NUMBER,
                STUDENT.SURNAME
            )
            .apply { students.forEach{ values(it.chatId, it.examYear, it.examType.toString(), it.passportSeries, it.passportNumber, it.surname) } }
            .onConflictDoNothing()
            .execute()
    }

    fun selectStudents(chatId: Long): List<Student> {
        return dslContext.select(
            STUDENT.ID,
            STUDENT.PASSPORT_NUMBER,
            STUDENT.PASSPORT_SERIES,
            STUDENT.CHAT_ID,
            STUDENT.EXAM_TYPE,
            STUDENT.SURNAME,
            STUDENT.EXAM_YEAR
        )
            .from(STUDENT)
            .where(STUDENT.CHAT_ID.eq(chatId))
            .fetchInto(Student::class.java)
    }

    fun selectStudents(ids: Set<Long>): List<Student> {
        return dslContext.select(
            STUDENT.ID,
            STUDENT.PASSPORT_NUMBER,
            STUDENT.PASSPORT_SERIES,
            STUDENT.CHAT_ID,
            STUDENT.EXAM_TYPE,
            STUDENT.SURNAME,
            STUDENT.EXAM_YEAR
        )
            .from(STUDENT)
            .where(STUDENT.ID.`in`(ids))
            .fetchInto(Student::class.java)
    }

    fun deleteStudents(ids: List<Long>) {
        dslContext.deleteFrom(STUDENT)
            .where(STUDENT.ID.`in`(ids))
            .execute()
    }

    fun update(students: List<Student>) {
        if (students.isEmpty()) {
            return
        }
        dslContext.insertInto(STUDENT)
            .columns(
                STUDENT.ID,
                STUDENT.CHAT_ID,
                STUDENT.EXAM_YEAR,
                STUDENT.EXAM_TYPE,
                STUDENT.PASSPORT_SERIES,
                STUDENT.PASSPORT_NUMBER,
                STUDENT.SURNAME
            )
            .apply { students.forEach{ values(it.id, it.chatId, it.examYear, it.examType.toString(), it.passportSeries, it.passportNumber, it.surname) } }
            .onConflict(STUDENT.ID)
            .doUpdate()
            .set(STUDENT.CHAT_ID, excluded(STUDENT.CHAT_ID))
            .set(STUDENT.EXAM_YEAR, excluded(STUDENT.EXAM_YEAR))
            .set(STUDENT.EXAM_TYPE, excluded(STUDENT.EXAM_TYPE))
            .set(STUDENT.PASSPORT_SERIES, excluded(STUDENT.PASSPORT_SERIES))
            .set(STUDENT.PASSPORT_NUMBER, excluded(STUDENT.PASSPORT_NUMBER))
            .set(STUDENT.SURNAME, excluded(STUDENT.SURNAME))
            .execute()
    }
}