package project.task_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@Getter
public class BaseEntity {

    @Column(name = "created_at")
    @CreatedDate
    private String createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private String updatedAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm")
        );
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm")
        );
    }
}
