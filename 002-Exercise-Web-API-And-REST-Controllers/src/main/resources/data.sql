INSERT INTO books (title, author, isbn)
VALUES ('Harry Potter', 'J. K. Rowling', '0-7475-3269-9'),
       ('Game of Thrones', 'George R. R. Martin', '9780553386790');

INSERT INTO book_entity_tags (book_entity_id, tags_order, tags)
VALUES (1, 0, 'fantasy'),
       (1, 1, 'magic'),
       (2, 0, 'fantasy');
