package com.mklinga.reflekt.contacts.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@Table(name = "contact_relations")
public class ContactRelation {
  @Id
  @GeneratedValue(generator = "contact_relation_id_gen")
  @SequenceGenerator(
      name = "contact_relation_id_gen",
      sequenceName = "contact_relation_id_seq",
      allocationSize = 1
  )
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "subject")
  private JpaContact subject;

  private RelationPredicate predicate;

  @ManyToOne
  @JoinColumn(name = "object")
  private JpaContact object;
}
