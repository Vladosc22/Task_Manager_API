package com.vlad.task_manager_api.task.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TaskRequest {
    @NotBlank(message = "title is required")
    public String title;

    public String description;

    @NotNull(message = "status is required")
    public String status; // or TaskStatus if you prefer

    @FutureOrPresent(message = "dueDate must be today or in the future")
    public LocalDate dueDate;
}