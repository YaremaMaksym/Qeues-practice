package com.xsakon.amqp;

public interface RabbitMQMessageProducer {
    void publish(Object payload, String exchange, String routingKey);
}
