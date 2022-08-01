package com.mklinga.reflekt.contacts.controllers;

import com.mklinga.reflekt.authentication.model.User;
import com.mklinga.reflekt.authentication.model.UserPrincipal;
import com.mklinga.reflekt.contacts.dtos.ContactEventDto;
import com.mklinga.reflekt.contacts.services.ContactEventService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestParam(name = "contactId", required = false) UUID contactId) {
    User user = userPrincipal.getUser();
    List<ContactEventDto> result = (contactId == null)
        ? contactEventService.findAll(user)
        : contactEventService.findForContactId(user, contactId);

    return ResponseEntity.ok(result);
  }

  @PostMapping("")
  public ResponseEntity<ContactEventDto> addNewContactEvent(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody ContactEventDto newContactEventDto
  ) {
    return ResponseEntity.of(
        contactEventService.addNewContactEvent(userPrincipal.getUser(), newContactEventDto));
  }
}
