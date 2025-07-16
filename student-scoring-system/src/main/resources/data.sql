-- Initial database setup
CREATE DATABASE IF NOT EXISTS studentscoring;

-- Create user if not exists
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'appuser') THEN
        CREATE USER appuser WITH PASSWORD 'apppassword';
    END IF;
END
$$;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE studentscoring TO appuser;

-- Sample data will be inserted by Hibernate