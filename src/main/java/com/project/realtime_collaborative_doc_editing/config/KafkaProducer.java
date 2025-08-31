//package com.project.realtime_collaborative_doc_editing.config;
//
//import com.project.realtime_collaborative_doc_editing.model.DocumentDetails;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaProducer {
//
//  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
//
//  private final KafkaTemplate<String,String> kafkaTemplate;
//
//  @Value("spring.kafka.topic.name")
//  private String topic;
//
//  public void sendMessage(String message){
//    LOGGER.info("Message sent through kafka is : {}",message);
//    kafkaTemplate.send("topic001",message);
//  }
//
//
//// this set of code is for, if we want to send Json object, we can use this in Rest API(Post/Put method is needed)
////    need a separate topic for this which will consume type of DocumentDetails
////    private final KafkaTemplate<String, DocumentDetails> kafkaTemplateJson;
////
////    public void sendJsonMessage(DocumentDetails data){
////        Message<DocumentDetails> message = MessageBuilder.withPayload(data)
////                .setHeader(KafkaHeaders.TOPIC,"topicName")
////                .build();
////        kafkaTemplate.send(message);
////    }
//
//
//}