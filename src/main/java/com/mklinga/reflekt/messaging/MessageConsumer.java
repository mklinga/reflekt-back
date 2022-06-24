package com.mklinga.reflekt.messaging;

import com.mklinga.reflekt.messaging.services.MessageService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

/**
 * MessageConsumer handles the fetch-handle-consume loop for the next available items in the
 * specific message queue.
 */
@Component
public class MessageConsumer {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final Integer defaultMaxMessages = 5;

  private final MessageService messageService;

  @Autowired
  public MessageConsumer(MessageService messageService) {
    this.messageService = messageService;
  }

  /**
   * Fetches $maxMessages from the $queueName (or less, if not enough messages available) and
   * loops over them, calling the given handler for each.
   *
   * @param queueName The name of the queue where we fetch the items
   * @param handler Handler to be called for each item
   * @param maxMessages Amount of items we are trying to fetch
   */
  public void consumeMessages(String queueName, MessageHandler handler, Integer maxMessages) {
    logger.info("Checking for new Messages in the queue " + queueName);
    List<Message> messages = messageService.getNextMessages(queueName, maxMessages);
    if (messages.isEmpty()) {
      logger.debug("No messages to consume, try sending some :)");
      return;
    }

    logger.debug("Found " + messages.size() + " new messages");
    messages.forEach(handler::handle);

    messageService.deleteMessages(queueName, messages);
    logger.info("Message consumer finished.");

    /* If we receive maximum amount of messages, we check immediately if there is more */
    if (messages.size() == maxMessages) {
      consumeMessages(queueName, handler);
    }
  }

  public void consumeMessages(String queueName, MessageHandler handler) {
    this.consumeMessages(queueName, handler, defaultMaxMessages);
  }

}
