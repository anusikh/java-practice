package com.anusikh.realtime.service;

import com.anusikh.realtime.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;

import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.time.Duration;
import java.util.logging.Level;

@Service
public class RabbitMqService {

    private String topicName = "notification-message";

    private final ReactiveRedisOperations<String, Message> redisOperations;

    private final Sender sender;
    private final Receiver receiver;

    public RabbitMqService(ReactiveRedisOperations<String, Message> redisOperations, Sender sender, Receiver receiver) {
        this.redisOperations = redisOperations;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Mono<Void> sendMessage(long userId, Message content) {
        String routingKey = topicName + '-' + userId;
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        byte[] messageSerialized = SerializationUtils.serialize(json);

        OutboundMessage message = new OutboundMessage(topicName, routingKey, messageSerialized);

        final Mono<AMQP.Exchange.DeclareOk> declareExchange = sender.declareExchange(
                ExchangeSpecification.exchange()
                        .name(topicName)
                        .durable(true)
                        .type("topic"));

        return declareExchange
                .flatMap(item -> sender.send(Mono.fromSupplier(() -> message)));
    }

    // hget Message 123
    public Mono<Void> insertAndNotify(long userId, Message message) {
        return redisOperations.opsForHash().put("Message", message.getId().toString(), message)
                .flatMap(x -> {
                    return this.sendMessage(userId, message);
                })
                .map(x -> {
                    return x;
                });
    }

    public Flux<Message> receive(long userId, Duration timeout, Integer maxMessageCount) {
        final String routingKey = topicName + "-" + userId;
        ObjectMapper objectMapper = new ObjectMapper();

        final Mono<AMQP.Queue.DeclareOk> declareQueue = sender
                .declareQueue(QueueSpecification.queue())
                .log("declare-queue", Level.FINER);

        final Mono<String> bindQueue = declareQueue
                .flatMap(declareOk -> sender.bindQueue(
                                BindingSpecification.binding()
                                        .queue(declareOk.getQueue())
                                        .exchange(topicName)
                                        .routingKey(routingKey))
                        .map(bindOk -> declareOk.getQueue()))
                // this code is for returning queueName instead of bind result
                .log("bind-queue", Level.FINER);

        Flux<Message> result = bindQueue
                .flatMapMany(receiver::consumeAutoAck)
                .map(item -> {
                    try {
                        // converting to Message class
                        return objectMapper.readValue(SerializationUtils.deserialize(item.getBody()).toString(), Message.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });

        if (timeout != null) {
            result = result.timeout(timeout);
        }

        if (maxMessageCount != null) {
            result = result.limitRequest(maxMessageCount);
        }

        return result;
    }
}
