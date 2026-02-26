package com.vlad.task_manager_api.task.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskResponse {
    public Long id;
    public String title;
    public String description;
    public String status;
    public LocalDate dueDate;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Long userId;
}