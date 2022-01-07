CREATE TABLE IF NOT EXISTS "entries" (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    entry_date DATE NOT NULL,
    entry TEXT NOT NULL,
    mood TEXT NOT NULL,
    title TEXT NOT NULL
);