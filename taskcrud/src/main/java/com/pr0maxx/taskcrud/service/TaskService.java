package com.pr0maxx.taskcrud.service;

import com.pr0maxx.taskcrud.aspect.Loggable;
import com.pr0maxx.taskcrud.model.Task;
import com.pr0maxx.taskcrud.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService{

    private final TaskRepository taskRepository;

    @Loggable
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Loggable
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Loggable
    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTask(id);
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setUserId(updatedTask.getUserId());
        return taskRepository.save(existingTask);
    }

    @Loggable
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Loggable
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
