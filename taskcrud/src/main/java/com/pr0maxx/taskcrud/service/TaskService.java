package com.pr0maxx.taskcrud.service;

import com.pr0maxx.taskcrud.aspect.Loggable;
import com.pr0maxx.taskcrud.dto.TaskRequest;
import com.pr0maxx.taskcrud.dto.TaskResponse;
import com.pr0maxx.taskcrud.model.Task;
import com.pr0maxx.taskcrud.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Loggable
    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUserId(request.getUserId());

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

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUserId(request.getUserId());

        Task updated = taskRepository.save(task);
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
        return response;
    }
}

