package project.task_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.task_app.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
