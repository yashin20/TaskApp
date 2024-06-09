package project.task_app.dto;

import lombok.Data;
import project.task_app.entity.Task;

@Data
public class TaskResponseDto {

    /**
     * Task 응답 DTO
     * <p>
     * 등록 요청 - return : id
     * <p>
     * 조회 요청 - return : id, title, content, memberId, createdAt, updatedAt
     * <p>
     * 수정 요청 - return : id, title, content, memberId, createdAt, updatedAt
     * <p>
     * 삭제 요청 - return : id
     */

    private Long id;
    private String title;
    private String content;

    private Long memberId;

    private String createdAt;
    private String updatedAt;

    // 등록 / 삭제 응답 생성자
    public TaskResponseDto(Long id) {
        this.id = id;
    }

    // 조회 / 수정 응답 생성자
    public TaskResponseDto(Task entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.memberId = entity.getMember().getId();

        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
