--liquibase formatted sql

--changeset dfirsa:create_main_schema_for_the_app
create schema if not exists results;