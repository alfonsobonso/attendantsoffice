insert into user(first_name, last_name, email, user_status, congregation_id, position, role, created_by_user_id, updated_by_user_id)
values ('Alfonzo', 'Bonzo', 'alfonzo.bonzo@example.com', 'DISABLED', 1, 'BAPTISEDBRO', 'ADMIN', 0, 0);

insert into congregation(name, created_by_user_id)
values ('Grimsby', 0), ('Hull', 0), ('Slough, North', 0);
