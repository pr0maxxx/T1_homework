package com.pr0maxx.taskcrud.service;

import com.pr0maxx.taskcrud.AbstractTestContainer;
import com.pr0maxx.taskcrud.dto.TaskResponse;
import com.pr0maxx.taskcrud.model.Task;
import com.pr0maxx.taskcrud.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ImportAutoConfiguration(exclude = MailSenderValidatorAutoConfiguration.class)
public class SpringTaskServiceTest extends AbstractTestContainer {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testGetTask_Success() {
        // Создаем и сохраняем задачу напрямую через репозиторий (в реальную БД)
        Task task = new Task();
        task.setTitle("Test title");
        task.setDescription("Test desc");
        task.setUserId(123L);
        task.setStatus("NEW");
        task = taskRepository.save(task);

        // Получаем через сервис
        TaskResponse response = taskService.getTask(task.getId());

        assertNotNull(response);
        assertEquals(task.getId(), response.getId());
        assertEquals("Test title", response.getTitle());
        assertEquals("Test desc", response.getDescription());
        assertEquals(123L, response.getUserId());
        assertEquals("NEW", response.getStatus());
    }

    @Test
    void testGetTask_NotFound() {
        Long notExistingId = 999999L;
        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.getTask(notExistingId));
        assertEquals("Task not found", ex.getMessage());
    }
}
