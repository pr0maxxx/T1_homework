package com.pr0maxx.taskcrud.service;

import com.pr0maxx.taskcrud.dto.TaskRequest;
import com.pr0maxx.taskcrud.dto.TaskResponse;
import com.pr0maxx.taskcrud.model.Task;
import com.pr0maxx.taskcrud.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGetTask_Success() {
        Task task = createTask(1L, createRequest());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTask(1L);

        assertEquals(task.getId(), response.getId());
        assertEquals(task.getTitle(), response.getTitle());
        assertEquals(task.getDescription(), response.getDescription());
        assertEquals(task.getUserId(), response.getUserId());
        assertEquals(task.getStatus(), response.getStatus());

        verify(taskRepository).findById(1L);
    }

    @Test
    void testGetTask_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.getTask(1L));
        assertEquals("Task not found", ex.getMessage());

        verify(taskRepository).findById(1L);
    }

    private TaskRequest createRequest() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test");
        request.setDescription("Test desc");
        request.setUserId(123L);
        request.setStatus("NEW");
        return request;
    }

    private Task createTask(Long id, TaskRequest request) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUserId(request.getUserId());
        task.setStatus(request.getStatus());
        return task;
    }
}