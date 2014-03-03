create table users (
  id                serial primary key,
  name              varchar(255),
  email             varchar(255) unique,
  password_hash     varchar(255),
  password_salt     varchar(255)
);

create table todos (
  id          serial primary key,
  name        varchar(255),
  completed   boolean default false,
  user_id     integer,

  constraint todos_user_id_fk foreign key (user_id)
  references users (id)
  on update cascade
  on delete cascade
);