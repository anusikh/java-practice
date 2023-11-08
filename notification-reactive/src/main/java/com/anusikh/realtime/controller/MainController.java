package com.anusikh.realtime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anusikh.realtime.model.Message;
import com.anusikh.realtime.service.RabbitMqService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MainController {

    @Autowired
    private RabbitMqService rabbitMqService;

    @PostMapping("/put")
    public Mono<Void> put(@RequestBody Message message) {
        return rabbitMqService.insertAndNotify(123, message);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> stream() {
        return rabbitMqService.receive(123, null, null);
    }
}
