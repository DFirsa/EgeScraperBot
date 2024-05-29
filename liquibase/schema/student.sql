--liquibase formatted sql

--changeset dfirsa:create_student_id_sequence
create sequence results.student_id;

--changeset dfirsa:create_student_table
create table results.student (
    id              bigint      not null default nextval('results.student_id') primary key,
    chat_id         bigint      not null,
    passport_series text        not null,
    passport_number text        not null,
    name            text,
    exam_type       text        not null,
    exam_year       smallint    not null,

    unique (chat_id, passport_series, passport_number),
    constraint fk_chat_id foreign key (chat_id) references results.chat (id)
);

--changeset dfirsa:create_student_year_idx
create index ix_student_year on results.student(exam_year);

--changeset dfirsa:add_on_delete_cascade_student
alter table results.student
drop constraint fk_chat_id,
add constraint fk_chat_id
    foreign key (chat_id) references results.chat (id) on delete cascade;

--changeset dfirsa:rename_name_to_surname
alter table results.student rename column name to surname;

--changeset dfirsa:not_null_surname
alter table results.student alter column surname set not null;

--changeset dfirsa:student_table_comments runOnChange:true
comment on table results.student is 'Students''es data';
comment on column results.student.id is 'Id of student';
comment on column results.student.chat_id is 'Id from table chat';
comment on column results.student.passport_series is 'Passport''s series';
comment on column results.student.passport_number is 'Passport''s number';
comment on column results.student.surname is 'Student''s surname';
comment on column results.student.exam_type is 'Type of exam (EGE/OGE)';
comment on column results.student.exam_year is 'Year when student passe''s exam';
