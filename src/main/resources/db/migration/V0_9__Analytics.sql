CREATE TABLE IF NOT EXISTS "analytics_journal_entry" (
    id UUID PRIMARY KEY,
    entry_id UUID REFERENCES "entries" (id) NOT NULL,
    word_count INT NOT NULL
);

CREATE TABLE IF NOT EXISTS "analytics_journal_user" (
    id UUID PRIMARY KEY,
    user_id INT REFERENCES "users" (id) NOT NULL,
    update_count INT NOT NULL
)
