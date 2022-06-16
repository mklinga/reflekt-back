package com.mklinga.reflekt.messaging;

import com.mklinga.reflekt.messaging.services.MessageService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
public class MessageConsumer {
  private Integer MAX_MESSAGES = 5;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final MessageService messageService;

  @Autowired
  public MessageConsumer(MessageService messageService) {
    this.messageService = messageService;
  }


  public void consumeMessages(MessageHandler handler) {
    logger.info("Checking for new Messages in the queue");
    List<Message> messages = messageService.getNextMessages(MAX_MESSAGES);
    if (messages.isEmpty()) {
      logger.debug("No messages to consume, try sending some :)");
      return;
    }

    logger.debug("Found " + messages.size() + " new messages");
    messages.stream().forEach(handler::handle);

    messageService.deleteMessages(messages);
    logger.info("Message consumer finished.");

    /* If we receive maximum amount of messages, we check immediately if there is more */
    if (messages.size() == MAX_MESSAGES) {
      consumeMessages(handler);
    }
  }
}
