drop schema read CASCADE;
drop schema public CASCADE;

create schema read;
create schema public;

delete read.user;
delete read.company_service;
delete read.company;

create EXTENSION IF NOT EXISTS "uuid-ossp";
