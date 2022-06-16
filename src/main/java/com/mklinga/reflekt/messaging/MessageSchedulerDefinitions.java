package com.mklinga.reflekt.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageSchedulerDefinitions {
  private final MessageConsumer messageConsumer;

  private final MessageHandler entrySavedMessageHandler = new EntrySavedMessageHandler();

  @Autowired
  public MessageSchedulerDefinitions(MessageConsumer messageConsumer) {
    this.messageConsumer = messageConsumer;
  }

  @Scheduled(cron = "0 */1 * * * *")
  public void handleEntrySavedMessages() {
    messageConsumer.consumeMessages(entrySavedMessageHandler);
  }
}
