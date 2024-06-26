/*
 * This file is generated by jOOQ.
 */
package ege.bot.service.codegen.models.keys


import ege.bot.service.codegen.models.tables.Chat
import ege.bot.service.codegen.models.tables.ChatPolls
import ege.bot.service.codegen.models.tables.ExamResult
import ege.bot.service.codegen.models.tables.Student
import ege.bot.service.codegen.models.tables.records.ChatPollsRecord
import ege.bot.service.codegen.models.tables.records.ChatRecord
import ege.bot.service.codegen.models.tables.records.ExamResultRecord
import ege.bot.service.codegen.models.tables.records.StudentRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val CHAT_PKEY: UniqueKey<ChatRecord> = Internal.createUniqueKey(Chat.CHAT, DSL.name("chat_pkey"), arrayOf(Chat.CHAT.ID), true)
val CHAT_POLLS_PKEY: UniqueKey<ChatPollsRecord> = Internal.createUniqueKey(ChatPolls.CHAT_POLLS, DSL.name("chat_polls_pkey"), arrayOf(ChatPolls.CHAT_POLLS.POLL_ID), true)
val EXAM_RESULT_PKEY: UniqueKey<ExamResultRecord> = Internal.createUniqueKey(ExamResult.EXAM_RESULT, DSL.name("exam_result_pkey"), arrayOf(ExamResult.EXAM_RESULT.EXAM_NAME, ExamResult.EXAM_RESULT.STUDENT_ID), true)
val STUDENT_CHAT_ID_PASSPORT_SERIES_PASSPORT_NUMBER_KEY: UniqueKey<StudentRecord> = Internal.createUniqueKey(Student.STUDENT, DSL.name("student_chat_id_passport_series_passport_number_key"), arrayOf(Student.STUDENT.CHAT_ID, Student.STUDENT.PASSPORT_SERIES, Student.STUDENT.PASSPORT_NUMBER), true)
val STUDENT_PKEY: UniqueKey<StudentRecord> = Internal.createUniqueKey(Student.STUDENT, DSL.name("student_pkey"), arrayOf(Student.STUDENT.ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val CHAT_POLLS__FK_CHAT_POLL_CHAT_ID: ForeignKey<ChatPollsRecord, ChatRecord> = Internal.createForeignKey(ChatPolls.CHAT_POLLS, DSL.name("fk_chat_poll_chat_id"), arrayOf(ChatPolls.CHAT_POLLS.CHAT_ID), ege.bot.service.codegen.models.keys.CHAT_PKEY, arrayOf(Chat.CHAT.ID), true)
val EXAM_RESULT__FK_STUDENT_ID: ForeignKey<ExamResultRecord, StudentRecord> = Internal.createForeignKey(ExamResult.EXAM_RESULT, DSL.name("fk_student_id"), arrayOf(ExamResult.EXAM_RESULT.STUDENT_ID), ege.bot.service.codegen.models.keys.STUDENT_PKEY, arrayOf(Student.STUDENT.ID), true)
val STUDENT__FK_CHAT_ID: ForeignKey<StudentRecord, ChatRecord> = Internal.createForeignKey(Student.STUDENT, DSL.name("fk_chat_id"), arrayOf(Student.STUDENT.CHAT_ID), ege.bot.service.codegen.models.keys.CHAT_PKEY, arrayOf(Chat.CHAT.ID), true)
