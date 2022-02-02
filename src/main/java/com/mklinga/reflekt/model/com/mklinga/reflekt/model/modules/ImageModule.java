package com.mklinga.reflekt.model.com.mklinga.reflekt.model.modules;

import com.mklinga.reflekt.model.JournalEntry;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

/**
 * ImageModule entity contains rows mapping journal entry into stored image names. One Entry may
 * have multiple images. Storing/fetching of the actual image data is handled by the /images/
 * endpoint.
 */
@Entity
@Table(name = "module_image_images")
@Getter
public class ImageModule {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn (name = "journal_entry", nullable = false)
  private JournalEntry journalEntry;

  @Column(name = "image_name", nullable = false)
  private String imageName;
}
