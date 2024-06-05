package project.task_app.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
}
