ALTER TABLE "entries" ADD COLUMN owner INT REFERENCES "users" (id);

-- One-time migrate for existing entries that were created before the column existed --
UPDATE entries SET owner = u.id FROM users u WHERE u.username = 'laite' AND owner IS NULL;

ALTER TABLE "entries" ALTER COLUMN owner SET NOT NULL;


