package com.mklinga.reflekt.contacts.utils;

import com.mklinga.reflekt.contacts.model.RelationPredicate;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RelationPredicateConverter implements AttributeConverter<RelationPredicate, String> {

  @Override
  public String convertToDatabaseColumn(RelationPredicate category) {
    if (category == null) {
      return null;
    }
    return category.getCode();
  }

  @Override
  public RelationPredicate convertToEntityAttribute(String code) {
    if (code == null) {
      return null;
    }

    return Stream.of(RelationPredicate.values()).filter(c -> c.getCode().equals(code)).findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
