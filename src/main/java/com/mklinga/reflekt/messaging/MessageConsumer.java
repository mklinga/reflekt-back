package com.mklinga.reflekt.messaging;

import com.mklinga.reflekt.services.MessageService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
public class MessageConsumer {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final MessageService messageService;

  @Autowired
  public MessageConsumer(MessageService messageService) {
    this.messageService = messageService;
  }

  private boolean handleMessage(Message message) {
    logger.info("Handling message " + message.messageId());
    /* TODO: Do something here */
    return true;
  }

  @Scheduled(cron = "0 */1 * * * *")
  public void consumeMessages() {
    logger.info("Checking for new Messages in the queue");
    List<Message> messages = messageService.getNextMessages();
    if (messages.isEmpty()) {
      logger.debug("No messages to consume, try sending some :)");
      return;
    }
    logger.debug("Found " + messages.size() + " new messages");
    for (Message m : messages) {
      handleMessage(m);
    }

    messageService.deleteMessages(messages);
    logger.info("Message consumer finished.");
  }
}
