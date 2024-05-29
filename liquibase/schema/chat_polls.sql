--liquibase formatted sql

--changeset dfirsa:create_chat_poll_table
create table results.chat_polls (
    poll_id bigint not null primary key,
    chat_id bigint not null,
    message_id bigint not null,

    constraint fk_chat_poll_chat_id foreign key (chat_id) references results.chat (id)
);

--changeset dfirsa:chat_poll_table_comments runOnChange:true
comment on table results.chat_polls is 'Information about active polls';
comment on column results.chat_polls.poll_id is 'Poll id';
comment on column results.chat_polls.chat_id is 'Chat with poll';
comment on column results.chat_polls.message_id is 'Message id with specific poll';