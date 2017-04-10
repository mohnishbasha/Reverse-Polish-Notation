# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table calculation (
  id                            bigint not null,
  input_equation                varchar(255),
  result                        double,
  created_date                  timestamp,
  session_token                 varchar(255),
  status                        varchar(255),
  constraint pk_calculation primary key (id)
);
create sequence calculation_seq;


# --- !Downs

drop table if exists calculation;
drop sequence if exists calculation_seq;

