package com.pr0maxx.taskcrud.service;

import com.pr0maxx.taskcrud.aspect.Loggable;
import com.pr0maxx.taskcrud.dto.TaskRequest;
import com.pr0maxx.taskcrud.dto.TaskResponse;
import com.pr0maxx.taskcrud.kafka.KafkaTaskProducer;
import com.pr0maxx.taskcrud.kafka.dto.TaskStatusUpdateMessage;
import com.pr0maxx.taskcrud.model.Task;
import com.pr0maxx.taskcrud.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final KafkaTaskProducer kafkaTaskProducer;

    @Loggable
    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUserId(request.getUserId());
        task.setStatus(request.getStatus());

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Loggable
    public TaskResponse getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return toResponse(task);
    }

    @Loggable
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        boolean statusChanged = !Objects.equals(task.getStatus(), request.getStatus());

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUserId(request.getUserId());
        task.setStatus(request.getStatus());

        Task updated = taskRepository.save(task);

        if (statusChanged) {
            TaskStatusUpdateMessage message = new TaskStatusUpdateMessage(
                    updated.getId(),
                    updated.getStatus()
            );
            kafkaTaskProducer.sendStatusUpdate(message);
        }
        return toResponse(updated);
    }

    @Loggable
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Loggable
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setUserId(task.getUserId());
        response.setStatus(task.getStatus());
        return response;
    }
}

