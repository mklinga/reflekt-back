package com.mklinga.reflekt.messaging;

import software.amazon.awssdk.services.sqs.model.Message;

public interface MessageHandler {
  void handle(Message message);
}
