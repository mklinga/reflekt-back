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

/**
 * Handling of the messages for all the journal entry - related topics.
 */
@Service
public class JournalEntryMessageService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final MessageService messageService;

  private final String entryUpdateMessage = "entry.update";

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

  private Message createUpdateMessage(Map<String, MessageAttributeValue> attributes) {
    return Message.builder()
        .body(entryUpdateMessage)
        .messageAttributes(attributes)
        .build();
  }

  /**
   * Sends the update message (this.entryUpdateMessage) for specific journal entry into the
   * messageservice. The messages will be grouped by the userId of the owner for entry.
   *
   * @param entry JournalEntry that has been updated
   */
  public void sendUpdateMessage(JournalEntry entry) {
    logger.info("Sending the update message for entry {}", entry.getId().toString());
    Map<String, MessageAttributeValue> attributes = new HashMap<>();

    attributes.put("entryId", createStringAttribute(entry.getId().toString()));
    attributes.put("userId", createStringAttribute(entry.getOwner().getId().toString()));

    String messageGroupId = Integer.toString(entry.getOwner().getId());
    messageService.sendMessage(entryUpdateQueue, createUpdateMessage(attributes), messageGroupId);
  }

}
