package project.task_app.controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.task_app.dto.MemberRequestDto;
import project.task_app.dto.TaskRequestDto;
import project.task_app.entity.Member;
import project.task_app.entity.Task;
import project.task_app.repository.MemberRepository;
import project.task_app.service.MemberService;
import project.task_app.service.TaskService;

import java.time.LocalDateTime;

// Test Data 생성
@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitUserService initUserService;


    @PostConstruct
    public void init() {
        initUserService.init();
    }


    @Component
    static class InitUserService {
        @PersistenceContext
        private EntityManager em;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private MemberService memberService;
        @Autowired
        private MemberRepository memberRepository;
        @Autowired
        private TaskService taskService;

        @Transactional
        public void init() {
            Long member1ID = memberService.createMember(new MemberRequestDto("member1",
                    "1q2w3e4r~!", "1q2w3e4r~!",
                    "member1@example.com", "010-1111-1111"));

            Long member2ID = memberService.createMember(new MemberRequestDto("member2",
                    "1q2w3e4r~!", "1q2w3e4r~!",
                    "member2@example.com", "010-1111-1111"));

/*            Long member3ID = memberService.createMember(new MemberRequestDto("member3",
                    "1q2w3e4r~!", "1q2w3e4r~!",
                    "wlddud8@gmail.com", "010-1111-1111"));*/

            for (int i = 0; i < 10; i++) {
                taskService.createTask(new TaskRequestDto("title" + i + "By member1", "content" + i,
                        memberRepository.findById(member1ID).get()));
                taskService.createTask(new TaskRequestDto("title" + i + "By member2", "content" + i,
                        memberRepository.findById(member2ID).get()));
            }
        }
    }
}
