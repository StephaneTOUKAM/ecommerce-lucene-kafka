package com.example.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaConfig {

    public static final String SEARCH_TOPIC = "product-search";

    @Bean
    public NewTopic searchTopic() {
        // Creates topic if auto.create.topics.enable=false on broker; optional
        return new NewTopic(SEARCH_TOPIC, 1, (short)1);
    }
}
