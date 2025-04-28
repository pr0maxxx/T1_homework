package com.pr0maxx.taskcrud.repository;


import com.pr0maxx.taskcrud.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
