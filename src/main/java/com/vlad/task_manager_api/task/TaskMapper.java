package com.vlad.task_manager_api.task;

import com.vlad.task_manager_api.task.dto.TaskRequest;
import com.vlad.task_manager_api.task.dto.TaskResponse;

public class TaskMapper {

    private TaskMapper() {} // prevent new TaskMapper()

    private static Task.TaskStatus parseStatus(String status) {
        if (status == null || status.isBlank()) return Task.TaskStatus.TODO;
        return Task.TaskStatus.valueOf(status.trim().toUpperCase());
    }

    public static Task toEntity(TaskRequest req) {
        Task t = new Task();
        t.setTitle(req.title);
        t.setDescription(req.description);
        t.setStatus(parseStatus(req.status));
        t.setDueDate(req.dueDate);
        return t;
    }

    public static void updateEntity(Task t, TaskRequest req) {
        t.setTitle(req.title);
        t.setDescription(req.description);
        t.setStatus(parseStatus(req.status));
        t.setDueDate(req.dueDate);
    }

    public static TaskResponse toResponse(Task t) {
        TaskResponse r = new TaskResponse();
        r.id = t.getId();
        r.title = t.getTitle();
        r.description = t.getDescription();
        r.status = (t.getStatus() != null) ? t.getStatus().name() : null;
        r.dueDate = t.getDueDate();
        r.createdAt = t.getCreatedAt();
        r.updatedAt = t.getUpdatedAt();
        r.userId = (t.getUser() != null) ? t.getUser().getId() : null;
        return r;
    }
}