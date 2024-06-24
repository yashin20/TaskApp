package project.task_app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.task_app.entity.Member;
import project.task_app.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByMember(Member member, Pageable pageable);

    Page<Task> findByMemberAndIsChecked(Member member, Boolean isChecked, Pageable pageable);

    List<Task> findByMember(Member member); //전체 Task 목록
    List<Task> findByMemberAndIsChecked(Member member, Boolean isChecked); //전체 Task 체크 목록
}
