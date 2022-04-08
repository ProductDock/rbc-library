CREATE TABLE book (
    id bigint not null auto_increment,
    title varchar(255) not null,
    author varchar(255) not null,
    cover varchar(255),
    PRIMARY KEY(id)
);