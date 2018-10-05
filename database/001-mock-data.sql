insert into user(first_name, last_name, email, user_status, congregation_id, position, role, created_by_user_id, updated_by_user_id)
values ('Alfonzo', 'Bonzo', 'alfonzo.bonzo@example.com', 'DISABLED', 1, 'BAPTISEDBRO', 'ADMIN', 0, 0);

insert into congregation(name, created_by_user_id)
values ('Grimsby', 0), ('Hull', 0), ('Slough, North', 0);

insert into event(name, location, start_date, end_date, event_status, created_by_user_id, updated_by_user_id)
values('Test event', 'Glasgow Hydro', '2018-06-01', '2018-06-03', 'ANNOUNCED', 0, 0);

insert into event_team(

insert into event_team (event_id, name, name_with_captain, created_by_user_id, updated_by_user_id)
values(1, 'Attendants HQ', 'Attendants HQ', 0, 0);
