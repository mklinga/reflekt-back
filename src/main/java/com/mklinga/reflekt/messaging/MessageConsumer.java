package com.mklinga.reflekt.messaging;

import com.mklinga.reflekt.messaging.services.MessageService;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
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

  private final Integer defaultMaxMessages = 10;
  private final Integer consumerThreadCount = 2;

  private final MessageService messageService;

  @Autowired
  public MessageConsumer(MessageService messageService) {
    this.messageService = messageService;
  }

  /**
   * Fetches $maxMessages from the $queueName (or less, if not enough messages available) and
   * loops over them, calling the given handler for each.
   *
   * @param queueName   The name of the queue where we fetch the items
   * @param handler     Handler to be called for each item
   * @param maxMessages Amount of items we are trying to fetch
   */
  public void consumeMessages(String queueName, MessageHandler handler, Integer maxMessages) {
    ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors
        .newFixedThreadPool(consumerThreadCount);

    boolean consumerDone = false;

    while (!consumerDone) {
      List<Message> messages = messageService.getNextMessages(queueName, maxMessages);
      if (messages.isEmpty()) {
        logger.debug("No messages in the messageService for " + queueName);
        return;
      }

      logger.debug("Found " + messages.size() + " new messages, consuming...");
      try {
        pool.invokeAll(
            messages.stream()
                .map(message -> (Callable<Void>) () -> {
                  logger.debug("Invoking handler for {}", message.messageId());
                  handler.handle(message);
                  logger.debug("Handler done for {}", message.messageId());
                  return null;
                })
                .collect(Collectors.toList()));

        messageService.deleteMessages(queueName, messages);
        logger.info("Message consumer finished.");
      } catch (InterruptedException e) {
        logger.error("Message Consumer caught en interrupted exception");
        logger.error(e.getMessage());
        e.printStackTrace();
      }

      /* If we receive maximum amount of messages, we check immediately if there is more */
      consumerDone = (messages.size() != maxMessages);
      logger.debug("Consumer done? {} ({})", Boolean.toString(consumerDone), messages.size());
    }
  }

  public void consumeMessages(String queueName, MessageHandler handler) {
    this.consumeMessages(queueName, handler, defaultMaxMessages);
  }

}
