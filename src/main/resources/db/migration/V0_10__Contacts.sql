CREATE TABLE IF NOT EXISTS "contacts" (
    id UUID PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    owner INT REFERENCES users (id)
);

CREATE INDEX contact_owner_idx ON contacts (owner);

CREATE SEQUENCE contact_relation_id_seq AS INT INCREMENT BY 1 MINVALUE 1;

CREATE TABLE IF NOT EXISTS "contact_relations" (
    id INT PRIMARY KEY,
    subject UUID REFERENCES contacts(id) NOT NULL,
    predicate TEXT NOT NULL,
    object UUID REFERENCES contacts(id) NOT NULL
);