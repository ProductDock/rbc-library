INSERT INTO book (title, author, cover) VALUES ('Idealan timski igrač', 'Patrik Lencioni', 'https://www.delfi.rs/_img/artikli/2019/11/idealan_timski_igrac_vv.jpg');

UPDATE book SET title='Book of Vaadin', author='Vaadin Team', cover='https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1353276778l/16157237.jpg'  WHERE id = 37;

INSERT INTO book (id, title, author, cover) VALUES (73, 'Deleted in prod', 'Deleted in prod', 'deleted in prod');
DELETE FROM book WHERE id = 73;