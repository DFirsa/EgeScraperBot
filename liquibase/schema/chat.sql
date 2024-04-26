--liquibase formatted sql

--changeset dfirsa:create_user_table
create table results.chat (
    id bigint not null primary key,
    chat_mode text not null
);

--changeset dfirsa:user_table_comments runOnChange:true
comment on table results.chat is 'Table with chats (e.g. users)';
comment on column results.chat.id is 'Chat id from telegram';
comment on column results.chat.chat_mode is 'Chat type (TEACHER_CHAT/USER_CHAT). If you''re teacher you''re able to get results for multiple students';
