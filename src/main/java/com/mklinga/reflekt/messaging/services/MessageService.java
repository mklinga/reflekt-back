package com.mklinga.reflekt.messaging.services;

import java.util.List;
import software.amazon.awssdk.services.sqs.model.Message;

/**
 * MessageService defines the interface all underlying implementations of message service must
 * implement.
 */
public interface MessageService {
  void sendMessage(String queueName, Message message, String messageGroupId);

  List<Message> getNextMessages(String queueName, Integer amount);

  void deleteMessages(String queueName, List<Message> messages);
}
