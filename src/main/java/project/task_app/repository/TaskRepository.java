package project.task_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.task_app.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
