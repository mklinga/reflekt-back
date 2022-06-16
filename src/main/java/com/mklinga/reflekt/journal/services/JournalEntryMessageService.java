package com.mklinga.reflekt.journal.services;

import com.mklinga.reflekt.journal.model.JournalEntry;
import com.mklinga.reflekt.messaging.services.MessageService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

@Service
public class JournalEntryMessageService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final MessageService messageService;

  private final String ENTRY_UPDATE_MESSAGE = "entry.update";

  @Value("${messaging.entryUpdate.queue}")
  private String entryUpdateQueue;

  @Autowired
  public JournalEntryMessageService(MessageService messageService) {
    this.messageService = messageService;
  }

  private MessageAttributeValue createStringAttribute(String value) {
    return MessageAttributeValue.builder()
        .dataType("String")
        .stringValue(value)
        .build();
  }

  private Message createMessage(String body, Map<String, MessageAttributeValue> attributes) {
    return Message.builder()
        .body(body)
        .messageAttributes(attributes)
        .build();
  }

  public void sendUpdateMessage(JournalEntry entry) {
    logger.info("Sending the update message for id " + entry.getId().toString());
    Map<String, MessageAttributeValue> attributes = new HashMap<>();

    attributes.put("entryId", createStringAttribute(entry.getId().toString()));
    attributes.put("userId", createStringAttribute(entry.getOwner().getId().toString()));

    String messageGroupId = Integer.toString(entry.getOwner().getId());
    messageService.sendMessage(
        entryUpdateQueue,
        createMessage(ENTRY_UPDATE_MESSAGE, attributes),
        messageGroupId);
  }

}
