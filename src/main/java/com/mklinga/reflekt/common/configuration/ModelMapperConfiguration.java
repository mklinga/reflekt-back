package com.mklinga.reflekt.common.configuration;

import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.model.ContactRelation;
import com.mklinga.reflekt.journal.dtos.ImageDataDto;
import com.mklinga.reflekt.journal.model.Image;
import org.modelmapper.ModelMapper;

/**
 * We use a singleton ModelMapper throughout the application and we can define the specific rules
 * here for the variable namings etc. that the default operation does not work with.
 */
public class ModelMapperConfiguration {
  /**
   * Instantiates the modelMapper and sets the necessary rules.
   *
   * @return ModelMapper instance
   */
  public static ModelMapper getModelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    /* Custom field mappings */

    /*
      Mapping Image.imageName -> ImageDataDto.name
     */

    modelMapper
        .typeMap(Image.class, ImageDataDto.class)
        .addMapping(Image::getImageName, ImageDataDto::setName);

    /*
      Our ContactRelation contains fields `subject` and `object` as full JpaContact objects, but we
      want to send only the ID's to the frontend for these in the ContactRelationDto.
     */

    modelMapper
        .typeMap(ContactRelation.class, ContactRelationDto.class)
        .addMappings(m -> m.map(src -> src.getObject().getId(), ContactRelationDto::setObject));

    modelMapper
        .typeMap(ContactRelation.class, ContactRelationDto.class)
        .addMappings(m -> m.map(src -> src.getSubject().getId(), ContactRelationDto::setSubject));

    return modelMapper;
  }
}
