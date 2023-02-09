package com.xsakon.user;


import com.xsakon.amqp.RabbitMQMessageProducer;
import com.xsakon.clients.notification.NotificationClient;
import com.xsakon.clients.notification.NotificationRequest;
import com.xsakon.user.exception.DuplicateResourceException;
import com.xsakon.user.exception.RequestValidationException;
import com.xsakon.user.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDAO;
    private final NotificationClient notificationClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;


    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.notification}")
    private String notificationQueue;

    @Value("${rabbitmq.routing-keys.internal-notification}")
    private String internalNotificationRoutingKey;

    public void sendNotificationRequest(NotificationRequest notificationRequest) {
        rabbitMQMessageProducer.publish(
                notificationRequest,
                internalExchange,
                internalNotificationRoutingKey
        );
    }

    public List<User> getAllUsers(){
        return userDAO.selectAllUsers();
    }

    public User getUserById(Integer id){
        return userDAO.selectUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "user with id [%s] not found".formatted(id)
                ));
    }

    public void addUser(UserRegistrationRequest registrationRequest){
        String email = registrationRequest.email();
        if(userDAO.existsUserWithEmail(email)){
            throw new DuplicateResourceException(
                    "Email already taken"
            );
        }

        User user = User.builder()
                .name(registrationRequest.name())
                .email(registrationRequest.email())
                .age(registrationRequest.age())
                .build();

        userDAO.insertUserAndFlush(user);

        NotificationRequest notificationRequest = new NotificationRequest(
                user.getId(),
                user.getEmail(),
                String.format("Hello %s, welcome to Xsakon...",
                        user.getName())
        );

        sendNotificationRequest(notificationRequest);
    }

    public void deleteUserById(Integer id){
        User user = userDAO.selectUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "user with id [%s] not found".formatted(id)
                ));

        userDAO.deleteUserById(id);

        NotificationRequest notificationRequest = new NotificationRequest(
                user.getId(),
                user.getEmail(),
                String.format("Hello %s, your account was successfully deleted...",
                        user.getName())
        );

        sendNotificationRequest(notificationRequest);
    }

    public void updateUserById(Integer id, UserUpdateRequest updateRequest){
        User user = userDAO.selectUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "user with id [%s] not found".formatted(id)
                ));

        boolean changes = false;

        if(updateRequest.name() != null && !updateRequest.name().equals(user.getName())){
            user.setName(updateRequest.name());
            changes = true;
        }

        if(updateRequest.age() != null && !updateRequest.age().equals(user.getAge())){
            user.setAge(updateRequest.age());
            changes = true;
        }

        if(updateRequest.email() != null && !updateRequest.email().equals(user.getEmail())){
            if(userDAO.existsUserWithEmail(updateRequest.email())){
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            user.setEmail(updateRequest.email());
            changes = true;
        }
        
        if(!changes){
            throw new RequestValidationException("no data changes found");
        }
        userDAO.updateUser(user);

        NotificationRequest notificationRequest = new NotificationRequest(
                user.getId(),
                user.getEmail(),
                String.format("Hello %s, your data changes was successfully saved",
                        user.getName())
        );

        sendNotificationRequest(notificationRequest);
    }
}
