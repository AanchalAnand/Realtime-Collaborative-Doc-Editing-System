package com.project.realtime_collaborative_doc_editing.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

  @Value("${spring.kafka.topic.name}")
  private String topic;

  @Bean
  public NewTopic topic(){
    return TopicBuilder.name("topic001")
        //.partitions(10) // we can make custom partition, currently using default partition
        .build();
  }

}