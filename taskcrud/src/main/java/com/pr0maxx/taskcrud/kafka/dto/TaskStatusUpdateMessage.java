package com.pr0maxx.taskcrud.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdateMessage {
    @JsonProperty("taskId")
    private Long taskId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("userId")
    private Long userId;
}
