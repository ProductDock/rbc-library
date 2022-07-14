CREATE TABLE topic (
   id bigint not null auto_increment,
   name varchar(255) not null,
   PRIMARY KEY(id)
);

INSERT INTO topic (name) VALUES ("SOFTWARE DEVELOPMENT");
INSERT INTO topic (name) VALUES ("PRODUCT MANAGEMENT");
INSERT INTO topic (name) VALUES ("MARKETING");
INSERT INTO topic (name) VALUES ("DESIGN");
INSERT INTO topic (name) VALUES ("PSYCHOLOGY");

CREATE TABLE book_topic (
    book_id bigint not null,
    topic_id bigint not null,
    PRIMARY KEY(book_id, topic_id)
);

INSERT into book_topic (book_id, topic_id) VALUES (1,2);
INSERT into book_topic (book_id, topic_id) VALUES (2,5);
INSERT into book_topic (book_id, topic_id) VALUES (2,2);
INSERT into book_topic (book_id, topic_id) VALUES (3,5);
INSERT into book_topic (book_id, topic_id) VALUES (4,3);
INSERT into book_topic (book_id, topic_id) VALUES (4,2);
INSERT into book_topic (book_id, topic_id) VALUES (5,2);
INSERT into book_topic (book_id, topic_id) VALUES (6,5);
INSERT into book_topic (book_id, topic_id) VALUES (7,1);
INSERT into book_topic (book_id, topic_id) VALUES (8,1);
INSERT into book_topic (book_id, topic_id) VALUES (9,1);
INSERT into book_topic (book_id, topic_id) VALUES (10,1);
INSERT into book_topic (book_id, topic_id) VALUES (11,2);
INSERT into book_topic (book_id, topic_id) VALUES (12,1);
INSERT into book_topic (book_id, topic_id) VALUES (13,2);
INSERT into book_topic (book_id, topic_id) VALUES (14,1);
INSERT into book_topic (book_id, topic_id) VALUES (14,2);
INSERT into book_topic (book_id, topic_id) VALUES (15,5);
INSERT into book_topic (book_id, topic_id) VALUES (16,1);
INSERT into book_topic (book_id, topic_id) VALUES (17,3);
INSERT into book_topic (book_id, topic_id) VALUES (17,2);
INSERT into book_topic (book_id, topic_id) VALUES (18,1);
INSERT into book_topic (book_id, topic_id) VALUES (19,1);
INSERT into book_topic (book_id, topic_id) VALUES (20,1);
INSERT into book_topic (book_id, topic_id) VALUES (21,1);
INSERT into book_topic (book_id, topic_id) VALUES (22,1);
INSERT into book_topic (book_id, topic_id) VALUES (23,1);
INSERT into book_topic (book_id, topic_id) VALUES (24,1);
INSERT into book_topic (book_id, topic_id) VALUES (25,1);
INSERT into book_topic (book_id, topic_id) VALUES (26,2);
INSERT into book_topic (book_id, topic_id) VALUES (26,3);
INSERT into book_topic (book_id, topic_id) VALUES (27,2);
INSERT into book_topic (book_id, topic_id) VALUES (28,1);
INSERT into book_topic (book_id, topic_id) VALUES (29,1);
INSERT into book_topic (book_id, topic_id) VALUES (30,2);
INSERT into book_topic (book_id, topic_id) VALUES (30,3);
INSERT into book_topic (book_id, topic_id) VALUES (31,2);
INSERT into book_topic (book_id, topic_id) VALUES (31,3);
INSERT into book_topic (book_id, topic_id) VALUES (32,2);
INSERT into book_topic (book_id, topic_id) VALUES (33,2);
INSERT into book_topic (book_id, topic_id) VALUES (34,5);
INSERT into book_topic (book_id, topic_id) VALUES (35,5);
INSERT into book_topic (book_id, topic_id) VALUES (36,2);
INSERT into book_topic (book_id, topic_id) VALUES (37,1);
INSERT into book_topic (book_id, topic_id) VALUES (38,2);
INSERT into book_topic (book_id, topic_id) VALUES (38,3);
INSERT into book_topic (book_id, topic_id) VALUES (39,2);
INSERT into book_topic (book_id, topic_id) VALUES (40,3);
INSERT into book_topic (book_id, topic_id) VALUES (41,1);
INSERT into book_topic (book_id, topic_id) VALUES (42,1);
INSERT into book_topic (book_id, topic_id) VALUES (43,1);
INSERT into book_topic (book_id, topic_id) VALUES (44,5);
INSERT into book_topic (book_id, topic_id) VALUES (45,2);
INSERT into book_topic (book_id, topic_id) VALUES (46,5);
INSERT into book_topic (book_id, topic_id) VALUES (47,5);
INSERT into book_topic (book_id, topic_id) VALUES (48,4);
INSERT into book_topic (book_id, topic_id) VALUES (49,5);
INSERT into book_topic (book_id, topic_id) VALUES (50,4);
INSERT into book_topic (book_id, topic_id) VALUES (51,2);
INSERT into book_topic (book_id, topic_id) VALUES (52,1);
INSERT into book_topic (book_id, topic_id) VALUES (53,5);
INSERT into book_topic (book_id, topic_id) VALUES (53,3);
INSERT into book_topic (book_id, topic_id) VALUES (54,2);
INSERT into book_topic (book_id, topic_id) VALUES (55,5);
INSERT into book_topic (book_id, topic_id) VALUES (56,1);
INSERT into book_topic (book_id, topic_id) VALUES (57,5);
INSERT into book_topic (book_id, topic_id) VALUES (58,5);
INSERT into book_topic (book_id, topic_id) VALUES (59,4);
INSERT into book_topic (book_id, topic_id) VALUES (60,5);
INSERT into book_topic (book_id, topic_id) VALUES (61,5);
INSERT into book_topic (book_id, topic_id) VALUES (62,2);
INSERT into book_topic (book_id, topic_id) VALUES (63,5);
INSERT into book_topic (book_id, topic_id) VALUES (64,5);
INSERT into book_topic (book_id, topic_id) VALUES (65,5);
INSERT into book_topic (book_id, topic_id) VALUES (66,2);
INSERT into book_topic (book_id, topic_id) VALUES (67,2);
INSERT into book_topic (book_id, topic_id) VALUES (68,1);
INSERT into book_topic (book_id, topic_id) VALUES (69,1);
INSERT into book_topic (book_id, topic_id) VALUES (70,1);
INSERT into book_topic (book_id, topic_id) VALUES (71,1);
INSERT into book_topic (book_id, topic_id) VALUES (72,5);