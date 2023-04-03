ALTER TABLE book_topic
ADD FOREIGN KEY (book_id) REFERENCES book(id);

ALTER TABLE book_topic
ADD FOREIGN KEY (topic_id) REFERENCES topic(id);
