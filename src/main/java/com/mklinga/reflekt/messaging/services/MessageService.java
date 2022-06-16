package com.mklinga.reflekt.messaging.services;

import java.util.List;
import software.amazon.awssdk.services.sqs.model.Message;

public interface MessageService {
  void sendMessage(Message message);
  List<Message> getNextMessages(Integer amount);
  void deleteMessages(List<Message> messages);
}
