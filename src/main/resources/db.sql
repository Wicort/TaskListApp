create table users (
                       id uuid not null primary key,
                       username varchar(100) not null,
                       email varchar(100) not null,
                       password varchar not null,
                       date_of_birth timestamp,
                       email_confirmed boolean,
                       created_at timestamp not null,
                       last_login_at timestamp
);

insert into users (id, username, email, password, date_of_birth, email_confirmed, created_at)
values ('d0754e6e-97c1-4ce0-a9be-3205b278c57a', 'wicort', 'wicort@yandex.ru', '123', timestamp '1985-08-01 00:00:00', false, now());


drop table task_item;
create table task_item (
    id uuid not null primary key ,
    task_name varchar(100) not null,
    task_description varchar(255),
    priority int,
    created_at timestamp not null,
    deadline_at  timestamp,
    released_at timestamp,
    user_id uuid references users(id) on delete cascade,
    author_id uuid references users(id) on delete SET NULL,
    editor_id uuid references users(id) on delete SET NULL,
    updated_at timestamp
)