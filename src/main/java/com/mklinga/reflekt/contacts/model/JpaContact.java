package com.mklinga.reflekt.contacts.model;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.business.Contact;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "contacts")
public class JpaContact extends Contact {
  @Id
  @Getter
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @ManyToOne
  @JoinColumn(name = "owner", nullable = false)
  private User owner;

  @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
  List<ContactRelation> relations;

  public JpaContact() {
    super();
  }

  public JpaContact(UUID id, FullName fullName, User owner, List<ContactRelation> relations) {
    super();
    this.id = id;
    this.setFullName(fullName);
    this.owner = owner;
    this.relations = relations;
  }

  public static JpaContact createDraftContact(FullName fullName, User owner) {
    return new JpaContact(Contact.draftId, fullName, owner, new ArrayList<>());
  }

  @Override
  public FullName getFullName() {
    return new FullName(this.firstName, this.lastName);
  }

  @Override
  protected void setFullName(FullName fullName) {
    this.firstName = fullName.getFirstName();
    this.lastName = fullName.getLastName();
  }

  @Override
  public List<ContactRelation> getRelations() {
    return this.relations;
  }

  @Override
  protected void setRelations(List<ContactRelation> relations) {
    this.relations = relations;
  }
}
