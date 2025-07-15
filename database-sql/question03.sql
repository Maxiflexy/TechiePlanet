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
(2004, 'Athens'),
(2008, 'Beijing'),
(2012, 'London'),
(2032, NULL);

INSERT INTO city (name, country) VALUES
('Sydney', 'Australia'),
('Athens', 'Greece'),
('Beijing', 'China'),
('London', 'UK');



-- ============================================
-- JOIN TYPES EXPLANATION
-- ============================================

-- INNER JOIN (for comparison baseline)
-- Returns only rows that have matching values in both tables
SELECT 'INNER JOIN EXAMPLE' as join_type;
SELECT
    g.yr,
    g.city,
    c.country
FROM games g
INNER JOIN city c ON g.city = c.name
ORDER BY g.yr;

-- Result: Only games with valid cities (2004, 2008, 2012)
-- Excludes: 2032 (no city) and Sydney (no game)




-- ============================================
-- LEFT JOIN EXPLANATION AND EXAMPLES
-- ============================================

SELECT 'LEFT JOIN EXPLANATION' as explanation;

-- LEFT JOIN (also called LEFT OUTER JOIN)
-- Returns ALL rows from the LEFT table (games)
-- Plus matching rows from the RIGHT table (city)
-- If no match exists, NULL values are returned for right table columns

SELECT 'LEFT JOIN EXAMPLE' as join_type;
SELECT
    g.yr,
    g.city,
    c.country
FROM games g
LEFT JOIN city c ON g.city = c.name
ORDER BY g.yr;

-- Result: ALL games are included
-- - 2004 Athens -> Greece (matched)
-- - 2008 Beijing -> China (matched)
-- - 2012 London -> UK (matched)
-- - 2032 NULL -> NULL (no match, but game year is preserved)



-- ============================================
-- RIGHT JOIN EXPLANATION AND EXAMPLES
-- ============================================

SELECT 'RIGHT JOIN EXPLANATION' as explanation;

-- RIGHT JOIN (also called RIGHT OUTER JOIN)
-- Returns ALL rows from the RIGHT table (city)
-- Plus matching rows from the LEFT table (games)
-- If no match exists, NULL values are returned for left table columns

SELECT 'RIGHT JOIN EXAMPLE' as join_type;
SELECT
    g.yr,
    g.city,
    c.name,
    c.country
FROM games g
RIGHT JOIN city c ON g.city = c.name
ORDER BY c.name;

-- Result: ALL cities are included
-- - NULL, NULL, Sydney, Australia (no game held in Sydney)
-- - 2004, Athens, Athens, Greece (matched)
-- - 2008, Beijing, Beijing, China (matched)
-- - 2012, London, London, UK (matched)

