CREATE TABLE IF NOT EXISTS "module_image_images" (
    id UUID PRIMARY KEY,
    image_name TEXT NOT NULL,
    journal_entry UUID REFERENCES entries (id)
);

CREATE INDEX m_image_images_entry_idx ON module_image_images (journal_entry);