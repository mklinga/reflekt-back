package com.mklinga.reflekt.contacts.model;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.contacts.business.Contact;
import com.mklinga.reflekt.contacts.interfaces.DraftableContact;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "contacts")
public class JpaContact extends Contact {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "job_title")
  private String jobTitle;

  @Column(name = "workplace")
  private String workplace;

  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "owner", nullable = false)
  private User owner;

  @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
  List<ContactRelation> relations;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
  Set<ContactEvent> contactEvents;

  public JpaContact() {
    super();
  }

  public JpaContact(UUID id, FullName fullName, User owner, List<ContactRelation> relations,
                    JobInformation jobInformation, String description) {
    super();
    this.id = id;
    this.setFullName(fullName);
    this.owner = owner;
    this.relations = relations;
    this.setJobInformation(jobInformation);
    this.description = description;
  }

  public static JpaContact createDraftContact(DraftableContact draftableContact, User owner) {
    return new JpaContact(
        UUID.randomUUID(),
        draftableContact.getFullName(),
        owner,
        new ArrayList<>(),
        draftableContact.getJobInformation(),
        draftableContact.getDescription()
    );
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public FullName getFullName() {
    return new FullName(this.firstName, this.lastName);
  }

  private void setFullName(FullName fullName) {
    this.firstName = fullName.getFirstName();
    this.lastName = fullName.getLastName();
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public JobInformation getJobInformation() {
    return new JobInformation(this.jobTitle, this.workplace);
  }

  private void setJobInformation(JobInformation jobInformation) {
    this.jobTitle = jobInformation.getJobTitle();
    this.workplace = jobInformation.getWorkplace();
  }

  @Override
  public User getOwner() {
    return this.owner;
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
