CREATE TABLE IF NOT EXISTS "contact_events" (
    id UUID PRIMARY KEY,
    event_type TEXT NOT NULL,
    event_date DATE NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS "contact_event_participants" (
    contact_id UUID REFERENCES contacts (id),
    contact_event_id UUID REFERENCES contact_events (id),
    PRIMARY KEY (contact_id, contact_event_id)
);