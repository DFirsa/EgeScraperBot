--liquibase formatted sql

--changeset dfirsa:create_exam-result_table
create table results.exam_result (
    exam_name           text    not null,
    absolute_result     int,
    is_passed_result    boolean,
    student_id          bigint  not null,

    primary key (exam_name, student_id),
    constraint fk_student_id foreign key (student_id) references results.student (id)
);

--changeset dfirsa:exam_result_table_comments runOnChange:true
comment on table results.exam_result is 'Results table';
comment on column results.exam_result.exam_name is 'Name of the exam';
comment on column results.exam_result.absolute_result is 'Result, which is represented via number [0..100]';
comment on column results.exam_result.absolute_result is 'Results, which is represented as exam passed or not';
comment on column results.exam_result.student_id is 'Student who''s result it is';