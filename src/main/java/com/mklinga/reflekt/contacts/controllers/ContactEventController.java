package com.mklinga.reflekt.contacts.controllers;

import com.mklinga.reflekt.authentication.model.UserPrincipal;
import com.mklinga.reflekt.contacts.dtos.ContactEventDto;
import com.mklinga.reflekt.contacts.services.ContactEventService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class ContactEventController {
  private final ContactEventService contactEventService;

  @Autowired
  public ContactEventController(ContactEventService contactEventService) {
    this.contactEventService = contactEventService;
  }

  @GetMapping("")
  public ResponseEntity<List<ContactEventDto>> getAllContactEvents(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    return ResponseEntity.ok(contactEventService.findAll(userPrincipal.getUser()));
  }
}
