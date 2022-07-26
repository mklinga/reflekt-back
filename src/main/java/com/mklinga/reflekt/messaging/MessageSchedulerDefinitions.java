package com.mklinga.reflekt.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Definitions of our @Scheduled message consumers.
 */
@Component
public class MessageSchedulerDefinitions {
  private final MessageConsumer messageConsumer;

  private final MessageHandler entrySavedMessageHandler;

  @Value("${messaging.entryUpdate.queue}")
  private String entryUpdateQueue;

  /**
   * Autowired constructor. Since we want (in the future) to provide multiple MessageHandlers and
   * assign them to individual schedulers, we use the @Qualifier annotation to tell Spring which
   * ones we want to specific variables.
   *
   * @param messageConsumer MessageConsumer bean
   * @param entrySavedMessageHandler MessageHandler for entry.update messages from the queue
   */
  @Autowired
  public MessageSchedulerDefinitions(
      MessageConsumer messageConsumer,
      @Qualifier("EntrySavedMessageHandler") MessageHandler entrySavedMessageHandler) {
    this.messageConsumer = messageConsumer;
    this.entrySavedMessageHandler = entrySavedMessageHandler;
  }

  @Scheduled(cron = "0 0 */3 * * *")
  // @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS, initialDelay = 30)
  public void handleEntryUpdatedMessages() {
    messageConsumer.consumeMessages(entryUpdateQueue, entrySavedMessageHandler);
  }
}
