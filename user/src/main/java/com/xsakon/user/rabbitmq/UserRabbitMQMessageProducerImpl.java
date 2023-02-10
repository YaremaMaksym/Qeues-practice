package com.xsakon.user.rabbitmq;

import com.xsakon.amqp.RabbitMQMessageProducer;
import com.xsakon.clients.notification.NotificationRequest;
import com.xsakon.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRabbitMQMessageProducerImpl implements RabbitMQMessageProducer {

    private final AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.notification}")
    private String notificationQueue;

    @Value("${rabbitmq.routing-keys.internal-notification}")
    private String internalNotificationRoutingKey;

    @Override
    public void publish(Object payload, String exchange, String routingKey){
        log.info("Publishing to {}, using routing key {}. Payload: {}", exchange, routingKey, payload);

        amqpTemplate.convertAndSend(exchange, routingKey, payload);

        log.info("Published to {}, using routing key {}. Payload: {}", exchange, routingKey, payload);
    }

    private NotificationRequest createNotificationRequest(User user, String message){
        return new NotificationRequest(
                user.getId(),
                user.getEmail(),
                String.format("Hello %s, %s",
                        user.getName(),
                        message)
        );
    }


    //todo: change type to DTO when password will added to User
    public void publishUserCreated(User createdUser){
        NotificationRequest notificationRequest = createNotificationRequest(
                createdUser,
                "your account was successfully created");

        publish(notificationRequest, internalExchange, internalNotificationRoutingKey);

    }

    public void publishUserUpdated(User updatedUser){
        NotificationRequest notificationRequest = createNotificationRequest(
                updatedUser,
                "your data changes were successfully saved");

        publish(notificationRequest, internalExchange, internalNotificationRoutingKey);

    }

    public void publishUserDeleted(User deletedUser){
        NotificationRequest notificationRequest = createNotificationRequest(
                deletedUser,
                "your account was successfully deleted");

        publish(notificationRequest, internalExchange, internalNotificationRoutingKey);
    }
}
