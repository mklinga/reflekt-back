package com.mklinga.reflekt.contacts.controllers;

import com.mklinga.reflekt.authentication.model.UserPrincipal;
import com.mklinga.reflekt.contacts.dtos.ContactDto;
import com.mklinga.reflekt.contacts.dtos.ContactRelationDto;
import com.mklinga.reflekt.contacts.services.ContactService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
public class ContactController {

  private final ContactService contactService;

  @Autowired
  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @GetMapping("")
  public ResponseEntity<List<ContactDto>> getContacts(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    return ResponseEntity.ok(contactService.getAllContacts(userPrincipal.getUser()));
  }
}
