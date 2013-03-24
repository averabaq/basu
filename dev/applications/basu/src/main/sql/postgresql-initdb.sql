-- creates the f4bpa user
create user f4bpa with password 'f4bpa%123$x';

-- creates the f4bpa database
create database f4bpa owner f4bpa encoding='UTF-8';

-- connects to the new database
\connect f4bpa f4bpa

-- creates the event-store scheme
create schema event_store;

-- creates the event-dw scheme
create schema event_dw;
