package com.pr0maxx.taskcrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr0maxx.taskcrud.AbstractTestContainer;
import com.pr0maxx.taskcrud.dto.TaskRequest;
import com.pr0maxx.taskcrud.dto.TaskResponse;
import com.pr0maxx.taskcrud.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = { MailSenderValidatorAutoConfiguration.class })
public class TaskControllerTest extends AbstractTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setUserId(1L);
        request.setStatus("NEW");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void testGetTaskById() throws Exception {
        // Сначала создаём задачу, чтобы получить ID
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setUserId(1L);
        request.setStatus("NEW");

        String responseContent = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TaskResponse response = objectMapper.readValue(responseContent, TaskResponse.class);

        // Теперь получаем задачу по ID
        mockMvc.perform(get("/tasks/{id}", response.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void testGetTaskByInvalidId() {
        Long invalidId = 999L;
        assertThrows(RuntimeException.class, () -> {
            taskService.getTask(invalidId);
        });
    }


    @Test
    void testUpdateTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Original Title");
        request.setDescription("Original Description");
        request.setUserId(1L);
        request.setStatus("NEW");

        String content = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        TaskResponse created = objectMapper.readValue(content, TaskResponse.class);

        // Обновление
        request.setTitle("Updated Title");

        mockMvc.perform(put("/tasks/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testDeleteTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("To be deleted");
        request.setDescription("...");
        request.setUserId(1L);
        request.setStatus("NEW");

        String content = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        TaskResponse created = objectMapper.readValue(content, TaskResponse.class);

        mockMvc.perform(delete("/tasks/{id}", created.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        if ("Task not found".equals(ex.getMessage())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
