CREATE TABLE user_credentials (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_email_format CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

COMMENT ON TABLE user_credentials IS 'Таблица учетных данных пользователей для аутентификации';
COMMENT ON COLUMN user_credentials.id IS 'Уникальный идентификатор пользователя (UUID)';
COMMENT ON COLUMN user_credentials.email IS 'Электронная почта пользователя (уникальный идентификатор)';
COMMENT ON COLUMN user_credentials.phone IS 'Номер телефона пользователя (опционально)';
COMMENT ON COLUMN user_credentials.created_at IS 'Дата и время создания записи';
COMMENT ON COLUMN user_credentials.updated_at IS 'Дата и время последнего обновления записи';