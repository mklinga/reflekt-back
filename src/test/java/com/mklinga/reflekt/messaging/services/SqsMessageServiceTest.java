package com.mklinga.reflekt.messaging.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.eventstream.MessageBuilder;

@ExtendWith(MockitoExtension.class)
class SqsMessageServiceTest {

  private SqsMessageService sqsMessageService;
  private final String queueName = "test-queue";
  private final String queueUrl = "test-queue-url";

  @Mock
  private SqsClient client;

  @BeforeEach
  public void init() {
    this.sqsMessageService = new SqsMessageService();
    this.sqsMessageService.setClient(client);

    GetQueueUrlResponse urlResponse = GetQueueUrlResponse.builder().queueUrl(queueUrl).build();
    Mockito.when(client.getQueueUrl(Mockito.any(GetQueueUrlRequest.class))).thenReturn(urlResponse);
  }

  @Test
  void sendMessageShouldFormatMessageRequestCorrectly() {
    String body = "test-body";
    String groupId = "123";

    String entryId = "entry-1";
    String userId = "user-2";

    Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
    messageAttributes.put(
        "entryId",
        MessageAttributeValue.builder().dataType("String").stringValue(entryId).build());
    messageAttributes.put(
        "userId",
        MessageAttributeValue.builder().dataType("String").stringValue(userId).build());

    Message message = Message.builder()
        .body(body)
        .messageAttributes(messageAttributes)
        .build();

    sqsMessageService.sendMessage(this.queueName, message, groupId);
    ArgumentCaptor<SendMessageRequest> request = ArgumentCaptor.forClass(SendMessageRequest.class);
    Mockito.verify(client).sendMessage(request.capture());

    SendMessageRequest actualRequest = request.getValue();
    assertEquals(actualRequest.queueUrl(), this.queueUrl);
    assertEquals(actualRequest.messageBody(), body);
    assertEquals(actualRequest.messageGroupId(), groupId);
    Map<String, MessageAttributeValue> values = actualRequest.messageAttributes();
    assertEquals(values.get("entryId").stringValue(), entryId);
    assertEquals(values.get("userId").stringValue(), userId);
  }

  @Test
  void getNextMessagesShouldFormatRequestCorrectly() {
    ReceiveMessageResponse receiveResponse = ReceiveMessageResponse
        .builder().messages(new ArrayList<>()).build();
    Mockito.when(client.receiveMessage(Mockito.any(ReceiveMessageRequest.class)))
        .thenReturn(receiveResponse);

    Random random = new Random();
    Integer amount = random.nextInt(5, 11);

    sqsMessageService.getNextMessages(this.queueName, amount);

    ArgumentCaptor<ReceiveMessageRequest> request =
        ArgumentCaptor.forClass(ReceiveMessageRequest.class);

    Mockito.verify(client).receiveMessage(request.capture());

    ReceiveMessageRequest actualRequest = request.getValue();
    assertEquals(actualRequest.maxNumberOfMessages(), amount);
    assertEquals(actualRequest.queueUrl(), this.queueUrl);
    assertIterableEquals(actualRequest.messageAttributeNames(), List.of("userId", "entryId"));
  }

  @Test
  void deleteMessagesShouldFormatRequestCorrectly() {
    DeleteMessageResponse deleteMessageResponse = DeleteMessageResponse.builder().build();
    Mockito.when(client.deleteMessage(Mockito.any(DeleteMessageRequest.class)))
        .thenReturn(deleteMessageResponse);

    String receiptHandle1 = "receipt-1";
    String receiptHandle2 = "receipt-2";

    List<Message> messages = List.of(
        Message.builder().receiptHandle(receiptHandle1).build(),
        Message.builder().receiptHandle(receiptHandle2).build()
    );
    sqsMessageService.deleteMessages(this.queueName, messages);

    ArgumentCaptor<DeleteMessageRequest> request = ArgumentCaptor.forClass(DeleteMessageRequest.class);
    Mockito.verify(client, Mockito.times(2)).deleteMessage(request.capture());

    DeleteMessageRequest actualRequest1 = request.getAllValues().get(0);
    assertEquals(actualRequest1.receiptHandle(), receiptHandle1);
    assertEquals(actualRequest1.queueUrl(), this.queueUrl);

    DeleteMessageRequest actualRequest2 = request.getAllValues().get(1);
    assertEquals(actualRequest2.queueUrl(), this.queueUrl);
    assertEquals(actualRequest2.receiptHandle(), receiptHandle2);
  }
}