package com.mklinga.reflekt.messaging;

import java.util.concurrent.Callable;
import software.amazon.awssdk.services.sqs.model.Message;

/**
 * Interface for MessageHandler that is being used when consuming the messages from message queue.
 */
public interface MessageHandler {
  Callable<Void> getHandler(Message message);
}
