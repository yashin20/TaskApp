package project.task_app.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import project.task_app.dto.MemberRequestDto;
import project.task_app.dto.TaskRequestDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Task extends BaseEntity{

    /**
     * task_id(PK)
     * title
     * content
     * member_id(FK)
     */

    @Id @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private String title; //일정명
    private String content; //설명

    //Task 의 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //FK Column
    private Member member;


    //Update (수정일자 업데이트)
    public void update(TaskRequestDto dto) {
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }
        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }

//        setUpdatedAt(LocalDateTime.now());
    }
}
