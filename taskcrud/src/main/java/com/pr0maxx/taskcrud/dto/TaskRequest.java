package com.pr0maxx.taskcrud.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private Long userId;
    private String status;
}
