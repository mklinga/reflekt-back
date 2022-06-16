package com.mklinga.reflekt.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.model.Message;

public class EntrySavedMessageHandler implements MessageHandler {
  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void handle(Message message) {
    logger.info("Handling message " + message.messageId() + ": " + message.body());
    /* TODO: Do something here */
  }
}
