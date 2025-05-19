package com.pr0maxx.taskcrud.kafka;

import com.pr0maxx.taskcrud.kafka.dto.TaskStatusUpdateMessage;
import com.pr0maxx.taskcrud.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTaskConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            id = "${t1.kafka.consumer.group-id}",
            topics = "${t1.kafka.topic.task_id_status_change}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TaskStatusUpdateMessage> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Task consumer: Обработка {} новых сообщений", messageList.size());
        try {
            for (TaskStatusUpdateMessage message : messageList) {
                notificationService.sendStatusUpdate(message); // обработка каждого
            }
        }
        finally {
            ack.acknowledge(); // подтверждаем обработку всех сообщений
        }
        log.debug("Task consumer: Сообщения успешно обработаны и подтверждены.");
    }
}