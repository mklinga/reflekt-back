package com.mklinga.reflekt.messaging;

import com.mklinga.reflekt.analytics.services.AnalyticsService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component("EntrySavedMessageHandler")
public class EntrySavedMessageHandler implements MessageHandler {

  private final AnalyticsService analyticsService;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  public EntrySavedMessageHandler(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @Override
  public void handle(Message message) {
    logger.info("Handling message " + message.messageId());

    Integer userId = Integer.parseInt(
        message.messageAttributes().get("userId").stringValue());

    analyticsService.increaseUserUpdateCount(userId);

  }
}
