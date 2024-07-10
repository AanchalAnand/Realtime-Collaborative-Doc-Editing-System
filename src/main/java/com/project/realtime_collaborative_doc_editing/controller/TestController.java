package com.project.realtime_collaborative_doc_editing.controller;

import com.project.realtime_collaborative_doc_editing.config.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

  private final KafkaProducer kafkaProducer;

  @GetMapping("/kafka/publish")
  public ResponseEntity<String> sendMessageToKafka(@RequestParam("msg") String message){
    kafkaProducer.sendMessage(message);
    return new ResponseEntity<>("Message sent to topic.", HttpStatus.OK);
  }

}


// Live Streaming data for Test API
// https://stream.wikimedia.org/v2/stream/recentchange