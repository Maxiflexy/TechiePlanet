CREATE TABLE sessions (
    id INTEGER NOT NULL PRIMARY KEY,
    userId INTEGER NOT NULL,
    duration DECIMAL NOT NULL
);

-- Insert the sample data
INSERT INTO sessions(id, userId, duration) VALUES(1, 1, 10);
INSERT INTO sessions(id, userId, duration) VALUES(2, 2, 18);
INSERT INTO sessions(id, userId, duration) VALUES(3, 1, 14);


-- Main query to find the average session duration for users with more than one session
SELECT
    userId AS UserId,
    AVG(duration) AS AverageDuration
FROM sessions
GROUP BY userId
HAVING COUNT(*) > 1
ORDER BY userId;