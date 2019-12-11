CREATE TABLE movies (
  id IDENTITY NOT NULL PRIMARY KEY,
  imdbID VARCHAR(50) NOT NULL,
  title VARCHAR(250) NOT NULL,
  year INT NOT NULL,
  description VARCHAR,
  UNIQUE KEY movies_imdbID_UNIQUE (imdbID)
);

INSERT INTO movies (imdbID, title, year, description) VALUES
  ('tt7286456', 'Joker', 2019, 'In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.'),
  ('tt0068646', 'The Godfather', 1972, 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.');