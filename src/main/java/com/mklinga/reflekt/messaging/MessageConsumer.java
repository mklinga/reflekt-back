package com.mklinga.reflekt.messaging;

import com.mklinga.reflekt.messaging.services.MessageService;
import java.util.List;
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
   * processes them concurrently in separate threads, based on the consumerThreadCount variable.
   * If the messageService returns the maximum amount of the messages, we will immediately try
   * to fetch and process the next patch, until the messageservice returns less than the maximum
   * amount. TODO: This can end up badly, if the processing cannot succeed for some reason, we
   * should have some sort of a release mechanism to make sure we're not in an infinite loop...
   *
   * @param queueName   The name of the queue where we fetch the items
   * @param handler     Handler to be called for each item
   * @param maxMessages Amount of items we are trying to fetch
   */
  public void consumeMessages(String queueName, MessageHandler handler, Integer maxMessages) {
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors
        .newFixedThreadPool(consumerThreadCount);

    boolean consumerDone = false;
    while (!consumerDone) {
      List<Message> messages = messageService.getNextMessages(queueName, maxMessages);
      logger.debug("Found {} new messages in the queue", messages.size());

      if (messages.isEmpty()) {
        break;
      }

      try {
        threadPoolExecutor
            .invokeAll(messages.stream().map(handler::getHandler).collect(Collectors.toList()));

        messageService.deleteMessages(queueName, messages);
        logger.info("Message consumer finished, processed {} messages.", messages.size());

        /* If we receive maximum amount of messages from the queue, we re-consume immediately */
      } catch (InterruptedException e) {
        logger.error("Message consumer caught an exception with the queue {}.", queueName);
        logger.error(e.getMessage());
        e.printStackTrace();
        break;
      }

      consumerDone = (messages.size() != maxMessages);
      logger.debug("Consumer done? {} ({})", Boolean.toString(consumerDone), messages.size());
    }

    threadPoolExecutor.shutdown();
  }

  public void consumeMessages(String queueName, MessageHandler handler) {
    this.consumeMessages(queueName, handler, defaultMaxMessages);
  }

}
