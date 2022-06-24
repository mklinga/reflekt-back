package com.mklinga.reflekt.messaging.services;

import java.util.List;
import java.util.UUID;
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

/**
 * SQS Implementation for the MessageService used in our application. The AWS credentials must be
 * provided through enviroment variables for the authentication. Note that the current
 * implementation is hard coded for the queues to be located in the EU_NORTH_1 region.
 */
@Service
public class SqsMessageService implements MessageService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private SqsClient sqsClient;

  private SqsClient getClient() {
    if (this.sqsClient != null) {
      return this.sqsClient;
    }

    SqsClient client = SqsClient.builder()
        .region(Region.EU_NORTH_1)
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();

    setClient(client);
    return client;
  }

  public void setClient(SqsClient sqsClient) {
    this.sqsClient = sqsClient;
  }

  private String getQueueUrl(SqsClient sqsClient, String queueName) {
    GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
        .queueName(queueName)
        .build();

    return sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();
  }

  @Override
  public void sendMessage(String queueName, Message message, String messageGroupId) {
    SqsClient sqsClient = getClient();

    try {
      UUID deduplicationId = UUID.randomUUID();

      SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
          .messageDeduplicationId(deduplicationId.toString())
          .messageGroupId(messageGroupId)
          .messageBody(message.body())
          .messageAttributes(message.messageAttributes())
          .queueUrl(getQueueUrl(sqsClient, queueName))
          .build();

      sqsClient.sendMessage(sendMessageRequest);
    } catch (SqsException e) {
      logger.error(e.getMessage());
    }
  }

  @Override
  public List<Message> getNextMessages(String queueName, Integer amount) {
    SqsClient sqsClient = getClient();

    try {
      ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
          .queueUrl(getQueueUrl(sqsClient, queueName))
          .maxNumberOfMessages(amount)
          // TODO: This list should be given as a method argument
          .messageAttributeNames(List.of("userId", "entryId"))
          .build();

      return sqsClient.receiveMessage(receiveMessageRequest).messages();

    } catch (SqsException e) {
      logger.error(e.awsErrorDetails().errorMessage());
      return null;
    }
  }


  @Override
  public void deleteMessages(String queueName, List<Message> messages) {
    SqsClient sqsClient = getClient();
    try {
      for (Message message : messages) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
            .queueUrl(getQueueUrl(sqsClient, queueName))
            .receiptHandle(message.receiptHandle())
            .build();
        sqsClient.deleteMessage(deleteMessageRequest);
      }
    } catch (SqsException e) {
      logger.error(e.awsErrorDetails().errorMessage());
    }
  }

}
