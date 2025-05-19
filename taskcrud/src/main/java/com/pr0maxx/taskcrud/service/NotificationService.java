package com.pr0maxx.taskcrud.service;

import com.pr0maxx.taskcrud.kafka.dto.TaskStatusUpdateMessage;
import lombok.RequiredArgsConstructor;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendStatusUpdate(TaskStatusUpdateMessage message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo("maxim.vai@yandex.ru");
        email.setSubject("Task Updated");

        StringBuilder body = new StringBuilder();
        body.append("Task ID: ").append(message.getTaskId()).append("\n")
                .append("Status: ").append(message.getStatus()).append("\n");

        email.setText(body.toString());

        mailSender.send(email);
    }
}
