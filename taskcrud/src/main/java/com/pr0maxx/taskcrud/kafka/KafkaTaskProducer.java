package com.pr0maxx.taskcrud.kafka;

import com.pr0maxx.taskcrud.kafka.dto.TaskStatusUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskProducer {

    private final KafkaTemplate<String, TaskStatusUpdateMessage> kafkaTemplate;
    private final String topic = "task-status-change-topic";

    public void sendStatusUpdate(TaskStatusUpdateMessage message) {
        try {
            log.info("Отправка сообщения в Kafka: {}", message);
            kafkaTemplate.send(topic, String.valueOf(message.getTaskId()), message);
            kafkaTemplate.flush();
            log.info("Сообщение отправлено в Kafka");
        } catch (Exception e) {
            log.error("Ошибка при отправке в Kafka: {}", e.getMessage(), e);
        }
    }
}
