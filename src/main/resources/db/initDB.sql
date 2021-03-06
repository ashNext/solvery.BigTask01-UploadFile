DROP TABLE files IF EXISTS;

CREATE TABLE files
(
    id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(255)    NOT NULL,
    file    BLOB(1024)         NOT NULL
);