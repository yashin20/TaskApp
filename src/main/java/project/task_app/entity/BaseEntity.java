package project.task_app.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
