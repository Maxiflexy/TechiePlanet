CREATE TABLE scores (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    value DECIMAL(5,2) NOT NULL CHECK (value >= 0 AND value <= 100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,

    UNIQUE(student_id, subject_id)
);

CREATE INDEX idx_scores_student_id ON scores(student_id);
CREATE INDEX idx_scores_subject_id ON scores(subject_id);
CREATE INDEX idx_scores_value ON scores(value);