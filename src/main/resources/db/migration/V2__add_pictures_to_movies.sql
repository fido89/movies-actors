CREATE TABLE movies_pictures (
  movie_id bigint,
  picture_url VARCHAR,
  PRIMARY KEY (movie_id, picture_url),
  foreign key (movie_id) references movies(id)
);

INSERT INTO movies_pictures (movie_id, picture_url) VALUES
  (1, 'https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SY1000_CR0,0,674,1000_AL_.jpg'),
  (1, 'https://m.media-amazon.com/images/M/MV5BZDllNjQ0N2YtMGUxMS00MDJhLTgyNjMtNjM4ZDU4NjY0ZjQ0XkEyXkFqcGdeQXVyODc0OTEyNDU@._V1_.jpg'),
  (2, 'https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SY1000_CR0,0,704,1000_AL_.jpg'),
  (2, 'https://m.media-amazon.com/images/M/MV5BNWZiNjczYWQtMmI5NS00Y2I4LTlmNDYtY2Q5OGIwMzMzM2M4XkEyXkFqcGdeQXVyNjQ2NDA2ODM@._V1_.jpg');