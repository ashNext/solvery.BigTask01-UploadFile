DELETE FROM files;
ALTER TABLE files ALTER COLUMN id RESTART WITH 0;

INSERT INTO files (name, file)
VALUES ('fileName1', ''),
       ('fileName2', '');