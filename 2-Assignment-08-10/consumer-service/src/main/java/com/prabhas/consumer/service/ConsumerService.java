package com.prabhas.consumer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

@Service
public class ConsumerService {

	
    @KafkaListener(topics = "myTopic", groupId = "my-group")
    public void consumeString(String message) {
    	System.out.println("Received message: " + message);
    }
}