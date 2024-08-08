INSERT INTO task_service.user(id, email, password, name)
VALUES (1, 'author@mail.ru', '123', 'author'),
       (2, 'performer@mail.ru', '123', 'performer');
SELECT SETVAL('task_service.user_id_seq', (SELECT MAX(id) FROM task_service.user));


INSERT INTO task_service.task(id, header, description, status, priority, created_date, author_id, performer_id)
VALUES (1, 'testHeader', 'testDescription', 'PENDING', 'LOW', current_timestamp , 1, 2);
SELECT SETVAL('task_service.task_id_seq', (SELECT MAX(id) FROM task_service.task));

INSERT INTO task_service.comment(id, text, created_date, task_id)
VALUES (1, 'testComment', current_timestamp, 1);
SELECT SETVAL('task_service.comment_id_seq', (SELECT MAX(id) FROM task_service.comment));