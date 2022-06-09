ALTER TABLE "module_tag_tags" RENAME TO "tags";
ALTER TABLE "module_tag_entries" RENAME TO "tag_entries";

ALTER INDEX "m_tag_tags_owner_idx" RENAME TO "tag_owner_idx";
ALTER INDEX "m_tag_entries_idx" RENAME TO "tag_entries_idx";

ALTER TABLE "module_image_images" RENAME TO "images";
ALTER INDEX "m_image_images_entry_idx" RENAME TO "image_entry_idx";