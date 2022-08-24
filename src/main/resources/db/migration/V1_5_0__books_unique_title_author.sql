ALTER TABLE book
ADD CONSTRAINT uq_title_author UNIQUE(title, author);