package com.vlad.task_manager_api.task;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> findAll() {
        return repo.findAll();
    }

    public Task create(Task task) {
        task.setId(null); // force insert
        return repo.save(task);
    }

    public Task findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }

    public Task update(Long id, Task updated) {
        Task existing = findById(id);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());

        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}