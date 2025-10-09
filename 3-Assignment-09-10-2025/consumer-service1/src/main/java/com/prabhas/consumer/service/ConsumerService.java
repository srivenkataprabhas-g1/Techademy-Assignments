package com.prabhas.consumer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ConsumerService {

	
    @KafkaListener(topics = "myTopic", groupId = "my-group")
    public void consumeString(String message) {
    	System.out.println("Received message: " + message);
    }
}