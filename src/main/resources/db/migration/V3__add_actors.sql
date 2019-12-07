CREATE TABLE actors (
  id IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  surname VARCHAR(250)
);

CREATE TABLE movies_actors(
  movie_id bigint,
  actor_id bigint,
  PRIMARY KEY (movie_id, actor_id),
  foreign key (movie_id) references movies(id),
  foreign key (actor_id) references actors(id)
);

INSERT INTO actors (name, surname) VALUES
  ('Robert', 'De Niro'),
  ('Joaquin', 'Phoenix'),
  ('Al', 'Pacino'),
  ('Marlon', 'Brando');

INSERT INTO movies_actors (movie_id, actor_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4);