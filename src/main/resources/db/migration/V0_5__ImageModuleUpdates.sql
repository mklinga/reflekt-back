ALTER TABLE "module_image_images" ADD COLUMN owner INT REFERENCES "users" (id);

-- Until now, we only have images by laite, so we can do this
UPDATE "module_image_images" SET owner = u.id FROM users u WHERE u.username = 'laite' AND owner IS NULL;

ALTER TABLE "module_image_images" ADD COLUMN deleted BOOLEAN;
UPDATE "module_image_images" SET deleted = FALSE WHERE deleted IS NULL;

ALTER TABLE "module_image_images" ADD COLUMN mime_type VARCHAR(16);

-- Until now, we have only supported JPEG images
UPDATE "module_image_images" SET mime_type = 'image/jpeg' WHERE mime_type IS NULL;
