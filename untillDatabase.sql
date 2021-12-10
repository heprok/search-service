drop schema read CASCADE;
drop schema public CASCADE;

create schema read;
create schema public;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
