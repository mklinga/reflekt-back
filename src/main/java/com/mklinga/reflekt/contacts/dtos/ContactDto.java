package com.mklinga.reflekt.contacts.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mklinga.reflekt.contacts.interfaces.DraftableContact;
import com.mklinga.reflekt.contacts.model.FullName;
import com.mklinga.reflekt.contacts.model.JobInformation;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ContactDto implements DraftableContact {
  private UUID id;
  private String firstName;
  private String lastName;
  private String workplace;
  private String jobTitle;
  private String description;

  private List<ContactRelationDto> relations;

  @JsonIgnore
  public FullName getFullName() {
    return new FullName(this.firstName, this.lastName);
  }

  public void setFullName(FullName fullName) {
    this.firstName = fullName.getFirstName();
    this.lastName = fullName.getLastName();
  }

  @JsonIgnore
  public JobInformation getJobInformation() {
    return new JobInformation(this.jobTitle, this.workplace);
  }

  public void setJobInformation(JobInformation jobInformation) {
    this.workplace = jobInformation.getWorkplace();
    this.jobTitle = jobInformation.getJobTitle();
  }
}
