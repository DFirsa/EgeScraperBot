package ege.bot.service.dao

import ege.bot.model.Result
import ege.bot.service.codegen.models.tables.references.EXAM_RESULT
import ege.bot.service.codegen.models.tables.references.STUDENT
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.jooq.impl.SQLDataType
import org.springframework.stereotype.Service

@Service
class ResultDao(
    val dslContext: DSLContext
) {

    fun getResults(chatId: Long): List<Result> {
       return dslContext.select(
            EXAM_RESULT.EXAM_NAME,
            EXAM_RESULT.IS_PASSED_RESULT,
            EXAM_RESULT.ABSOLUTE_RESULT,
            EXAM_RESULT.STUDENT_ID
        )
            .from(EXAM_RESULT)
            .where(EXAM_RESULT.STUDENT_ID.`in`(
                select(STUDENT.ID)
                    .from(STUDENT)
                    .where(STUDENT.CHAT_ID.eq(chatId))
            ))
            .fetch()
            .map { Result(
                it.getValue(EXAM_RESULT.EXAM_NAME)!!,
                it.getValue(EXAM_RESULT.IS_PASSED_RESULT),
                it.getValue(EXAM_RESULT.ABSOLUTE_RESULT),
                it.getValue(EXAM_RESULT.STUDENT_ID)
            ) }
    }

    fun insert(results: List<Result>) {
        if (results.isEmpty()) {
            return
        }

        dslContext.insertInto(EXAM_RESULT)
            .columns(
                EXAM_RESULT.EXAM_NAME,
                EXAM_RESULT.STUDENT_ID,
                EXAM_RESULT.IS_PASSED_RESULT,
                EXAM_RESULT.ABSOLUTE_RESULT
            )
            .apply { results.forEach { values(it.subject, it.studentId!!, it.testResult, it.pointsResult) } }
            .onConflict(EXAM_RESULT.EXAM_NAME, EXAM_RESULT.STUDENT_ID)
            .doUpdate()
            .set(EXAM_RESULT.ABSOLUTE_RESULT, excluded(EXAM_RESULT.ABSOLUTE_RESULT))
            .set(EXAM_RESULT.IS_PASSED_RESULT, excluded(EXAM_RESULT.IS_PASSED_RESULT))
            .set(EXAM_RESULT.IS_PASSED_RESULT, false)
            .where(excluded(EXAM_RESULT.ABSOLUTE_RESULT).notEqual(EXAM_RESULT.ABSOLUTE_RESULT))
            .or(excluded(EXAM_RESULT.IS_PASSED_RESULT).notEqual(EXAM_RESULT.IS_PASSED_RESULT))
            .execute()
    }

    fun deleteResults(studentIds: List<Long>) {
        if (studentIds.isEmpty()) {
            return
        }

        dslContext.deleteFrom(EXAM_RESULT)
            .where(EXAM_RESULT.STUDENT_ID.`in`(studentIds))
            .execute()
    }

    fun getNotReportedResults(): List<Result> {
        return dslContext.select(
            EXAM_RESULT.EXAM_NAME,
            EXAM_RESULT.IS_PASSED_RESULT,
            EXAM_RESULT.ABSOLUTE_RESULT,
            EXAM_RESULT.STUDENT_ID
        )
            .from(EXAM_RESULT)
            .where(not(EXAM_RESULT.IS_REPORTED))
            .forUpdate()
            .fetch()
            .map { Result(
                it.getValue(EXAM_RESULT.EXAM_NAME)!!,
                it.getValue(EXAM_RESULT.IS_PASSED_RESULT),
                it.getValue(EXAM_RESULT.ABSOLUTE_RESULT),
                it.getValue(EXAM_RESULT.STUDENT_ID)
            ) }
    }

    fun markAsProcessed(results: List<Result>) {
        if (results.isEmpty()) {
            return
        }

        val rows = results.map { row(it.studentId!!, it.subject) }.toTypedArray()
        val keys = values(*rows).`as`("keys", "student_id", "subject")

        dslContext.update(EXAM_RESULT)
            .set(EXAM_RESULT.IS_REPORTED, true)
            .from(keys)
            .where(
                EXAM_RESULT.EXAM_NAME.eq(keys.field("subject", SQLDataType.VARCHAR))
                    .and(EXAM_RESULT.STUDENT_ID.eq(keys.field("student_id", SQLDataType.BIGINT)))
            ).execute()
    }
}