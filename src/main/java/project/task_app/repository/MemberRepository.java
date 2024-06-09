package project.task_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.task_app.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);
}
