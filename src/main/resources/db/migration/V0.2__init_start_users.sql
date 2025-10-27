SET
search_path TO event;

insert into users(uuid)
values ('8a8ac7b4-6e1f-4677-ba83-e4acb8559a7b'),
       ('bf5b1024-ca51-4744-9e61-0d2177ca4b80');

insert into events(user_id, name, date, type, description)
values (1, 'Wedding', current_timestamp, 'ONCE', 'description');
