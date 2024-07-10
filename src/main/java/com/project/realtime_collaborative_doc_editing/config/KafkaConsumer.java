package com.project.realtime_collaborative_doc_editing.config;

import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

  @KafkaListener(topics = "topic001", groupId = "group001")
  public void consumeMessage(String message){
    LOGGER.info("Message Received : {}",message);
  }

  @KafkaListener(topics = "JsonTopic001", groupId = "jsonGroup001")
  public void consumeJsonMessage(DocumentDetails message){
    LOGGER.info("Message Received : {}",message);
  }

}