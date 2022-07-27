package com.mklinga.reflekt.contacts.utils;

import com.mklinga.reflekt.contacts.model.ContactEventType;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class ContactEventTypeConverter implements AttributeConverter<ContactEventType, String> {

  @Override
  public String convertToDatabaseColumn(ContactEventType eventType) {
    if (eventType == null) {
      return null;
    }
    return eventType.getType();
  }

  @Override
  public ContactEventType convertToEntityAttribute(String type) {
    if (type == null) {
      return null;
    }

    return Stream.of(ContactEventType.values()).filter(c -> c.getType().equals(type)).findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
