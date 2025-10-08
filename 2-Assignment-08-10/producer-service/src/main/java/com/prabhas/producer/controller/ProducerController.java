package com.prabhas.producer.controller;

import com.prabhas.producer.service.ProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kafka")
public class ProducerController {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/publish/{message}")
    public String publishMessage(@PathVariable String message) {
        producerService.sendMessage(message);
        return "Message sent to Kafka topic: " + message;
    }
}