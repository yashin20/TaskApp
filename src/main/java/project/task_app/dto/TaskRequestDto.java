package project.task_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.task_app.entity.Member;
import project.task_app.entity.Task;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDto {

    public interface Create {}
    public interface Update {}

    /**
     * Task 요청 DTO
     * <p>
     * 등록 요청 : title, content, member
     * <p>
     * 조회 요청 : id
     * <p>
     * 수정 요청 : id, title, content
     * <p>
     * 삭제 요청 : id
     */

    @NotNull(groups = {Update.class}, message = "Title 은 필수 입력 값 입니다.")
    private Long id;

    @NotBlank(groups = {Create.class, Update.class}, message = "Title 은 필수 입력 값 입니다.")
    private String title;
    @NotBlank(groups = {Create.class, Update.class}, message = "Content 는 필수 입력 값 입니다.")
    private String content;

    private Member member;


    // 조회 / 삭제 요청 생성자
    public TaskRequestDto(Long id) {
        this.id = id;
    }

    // 등록 요청 생성자
    public TaskRequestDto(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    // 수정 요청 생성자
    public TaskRequestDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    //DTO -> Entity
    public Task toEntity() {
        return new Task(
                this.id,
                this.title,
                this.content,
                this.member
        );
    }

}
