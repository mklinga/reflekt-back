package com.mklinga.reflekt.model;

import java.util.Map;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

public record Message(String body, Map<String, MessageAttributeValue> attributes) {}
