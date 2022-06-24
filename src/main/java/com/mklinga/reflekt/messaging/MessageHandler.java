package com.mklinga.reflekt.messaging;

import software.amazon.awssdk.services.sqs.model.Message;

/**
 * Interface for MessageHandler that is being used when consuming the messages from message queue.
 */
public interface MessageHandler {
  void handle(Message message);
}
