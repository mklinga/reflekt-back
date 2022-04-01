CREATE TABLE IF NOT EXISTS "module_tag_tags" (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    owner INT REFERENCES users (id)
);

CREATE INDEX m_tag_tags_owner_idx ON module_tag_tags (owner);

CREATE TABLE IF NOT EXISTS "module_tag_entries" (
    tag_id UUID REFERENCES module_tag_tags (id),
    entry_id UUID REFERENCES entries (id),
    PRIMARY KEY (tag_id, entry_id)
);

CREATE INDEX m_tag_entries_idx ON module_tag_entries (tag_id, entry_id);
