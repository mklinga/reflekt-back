package com.mklinga.reflekt.messaging.services;

import com.mklinga.reflekt.messaging.services.MessageService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Service
public class SqsMessageService implements MessageService {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private SqsClient getClient() {
    return SqsClient.builder()
        .region(Region.EU_NORTH_1)
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();
  }

  private String getQueueUrl(SqsClient sqsClient) {
    GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
        .queueName("ReflektDevQueue")
        .build();

    return sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();
  }

  @Override
  public void sendMessage(Message message) {
    SqsClient sqsClient = getClient();

    try {

      SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
          .messageBody(message.body())
          .messageAttributes(message.messageAttributes())
          .queueUrl(getQueueUrl(sqsClient))
          .build();

      sqsClient.sendMessage(sendMessageRequest);
    } catch (SqsException e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public List<Message> getNextMessages(Integer amount) {
    SqsClient sqsClient = getClient();

    try {
      ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
          .queueUrl(getQueueUrl(sqsClient))
          .maxNumberOfMessages(amount)
          .build();

      return sqsClient.receiveMessage(receiveMessageRequest).messages();

    } catch (SqsException e) {
      logger.error(e.awsErrorDetails().errorMessage());
      return null;
    }
  }


  @Override
  public void deleteMessages(List<Message> messages) {
    SqsClient sqsClient = getClient();
    try {
      for (Message message : messages) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
            .queueUrl(getQueueUrl(sqsClient))
            .receiptHandle(message.receiptHandle())
            .build();
        sqsClient.deleteMessage(deleteMessageRequest);
      }
    } catch (SqsException e) {
      logger.error(e.awsErrorDetails().errorMessage());
    }
  }

}
