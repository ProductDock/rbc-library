CREATE TABLE review (
    book_id bigint not null,
    user_id varchar(100) not null,
    user_full_name varchar(255) not null,
    comment varchar(500),
    rating smallint,
    recommendation int,
    date timestamp,
    PRIMARY KEY(book_id, user_id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);