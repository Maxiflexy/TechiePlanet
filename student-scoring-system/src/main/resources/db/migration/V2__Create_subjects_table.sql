CREATE TABLE subjects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
);

-- Insert default subjects
INSERT INTO subjects (name, description) VALUES
('Mathematics', 'Mathematics subject'),
('Physics', 'Physics subject'),
('Chemistry', 'Chemistry subject'),
('Biology', 'Biology subject'),
('English', 'English subject');