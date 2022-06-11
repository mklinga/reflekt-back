package com.mklinga.reflekt.services;

import com.mklinga.reflekt.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Service
public class SqsMessageService implements MessageService {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private SqsClient getClient() {
    SqsClient sqsClient = SqsClient.builder()
        .region(Region.EU_NORTH_1)
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();

    return sqsClient;
  }

  @Override
  public void sendMessage(Message message) {
    SqsClient sqsClient = getClient();

    try {
      GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
          .queueName("ReflektDevQueue")
          .build();

      String queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();

      SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
          .messageBody(message.body())
          .messageAttributes(message.attributes())
          .queueUrl(queueUrl)
          .build();

      sqsClient.sendMessage(sendMessageRequest);
    } catch (SqsException e) {
      logger.error(e.getMessage());
    }

  }
}
