CREATE TABLE IF NOT EXISTS refresh_tokens (
   id SERIAL PRIMARY KEY,
   user_id BIGINT NOT NULL,
   token VARCHAR(255) NOT NULL,
   created_at TIMESTAMP DEFAULT current_timestamp
);

CREATE INDEX idx_created_at ON refresh_tokens (created_at);

CREATE OR REPLACE FUNCTION cleanup_refresh_tokens()
    RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM refresh_tokens
    WHERE created_at < (NOW() - INTERVAL '30 days');
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER cleanup_refresh_tokens_trigger
    AFTER INSERT ON refresh_tokens
EXECUTE FUNCTION cleanup_refresh_tokens();