CREATE TABLE IF NOT EXISTS "users_contacts" (
    user_id INT REFERENCES users (id) PRIMARY KEY,
    contact_id UUID REFERENCES contacts (id)
);

-- Insert initial data for the dev user (id=2)
INSERT INTO contacts (id, first_name, last_name, owner) VALUES ('8da601b5-73a6-46fc-9a04-d39ea1cc9c27', 'You', '', 2);
INSERT INTO "users_contacts" (user_id, contact_id) VALUES (2, '8da601b5-73a6-46fc-9a04-d39ea1cc9c27');
