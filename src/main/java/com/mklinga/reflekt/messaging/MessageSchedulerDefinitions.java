package com.mklinga.reflekt.messaging;

import com.mklinga.reflekt.analytics.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageSchedulerDefinitions {
  private final MessageConsumer messageConsumer;

  private final AnalyticsService analyticsService;
  private final MessageHandler entrySavedMessageHandler;

  @Autowired
  public MessageSchedulerDefinitions(
      MessageConsumer messageConsumer,
      AnalyticsService analyticsService,
      @Qualifier("EntrySavedMessageHandler") MessageHandler entrySavedMessageHandler) {
    this.messageConsumer = messageConsumer;
    this.analyticsService = analyticsService;
    this.entrySavedMessageHandler = entrySavedMessageHandler;
  }

  @Scheduled(cron = "0 */1 * * * *")
  public void handleEntrySavedMessages() {
    messageConsumer.consumeMessages(entrySavedMessageHandler);
  }
}
