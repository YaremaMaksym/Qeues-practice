package com.xsakon.notification;

import com.xsakon.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest){
        notificationRepository.save(Notification.builder()
                .toUserId(notificationRequest.toUserId())
                .toUserEmail(notificationRequest.toUserEmail())
                .message(notificationRequest.message())
                .senderName("Xsakon")
                .sentAt(LocalDateTime.now()).build());
    }
}
