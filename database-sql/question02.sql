CREATE TABLE games (
    yr INTEGER,
    city VARCHAR(50)
);

CREATE TABLE city (
    name VARCHAR(50),
    country VARCHAR(50)
);

-- Insert data from the example
INSERT INTO games (yr, city) VALUES
(1896, 'Athens'),
(1948, 'London'),
(2004, 'Athens'),
(2008, 'Beijing'),
(2012, 'London');

INSERT INTO city (name, country) VALUES
('Sydney', 'Australia'),
('Athens', 'Greece'),
('Beijing', 'China'),
('London', 'UK');





-- Query to find the year, host city, and host country of each Olympic Games
SELECT
    games.yr AS game_year,
    games.city AS host_city,
    city.country AS host_country
FROM games
INNER JOIN city ON games.city = city.name
ORDER BY games.yr;
