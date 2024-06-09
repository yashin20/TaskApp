package project.task_app.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.task_app.dto.TaskRequestDto;
import project.task_app.dto.TaskResponseDto;
import project.task_app.entity.Member;
import project.task_app.entity.Task;
import project.task_app.exception.DataNotFoundException;
import project.task_app.repository.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class TaskServiceTest {

    @Autowired private TaskService taskService;
    @Autowired private TaskRepository taskRepository;
    @Autowired private EntityManager em;


    @Test
    @DisplayName("Task 등록 테스트")
    public void createTaskTest() throws Exception {
        //given
        //1. member 생성
        Member member = new Member("member", "password", "email@example.com", "010-1111-1111");
        em.persist(member); em.flush();

        //2. requestDto 생성
        TaskRequestDto requestDto = new TaskRequestDto("title", "content", member);

        //when
        //3. task 등록
        Long taskId = taskService.createTask(requestDto);
        Task findTask = taskRepository.findById(taskId).get();

        //then
        assertNotNull(findTask);
        assertEquals("title", findTask.getTitle());
        assertEquals("content", findTask.getContent());
        assertEquals(member, findTask.getMember());
    }

    @Test
    @DisplayName("Task 정보 조회 테스트")
    public void readTaskTest() throws Exception {
        //given
        //1. member 생성
        Member member = new Member("member", "password", "email@example.com", "010-1111-1111");
        em.persist(member); em.flush();
        Long taskId = taskService.createTask(new TaskRequestDto("title", "content", member));

        //2. requestDto 생성
        TaskRequestDto requestDto = new TaskRequestDto(taskId);

        //when
        //3. task Information 불러오기
        TaskResponseDto responseDto = taskService.getTaskInformation(requestDto);
        Task findTask = taskRepository.findById(responseDto.getId()).get();

        //then
        assertThat(responseDto.getId()).isEqualTo(taskId);
        assertThat(responseDto.getTitle()).isEqualTo("title");
        assertThat(responseDto.getContent()).isEqualTo("content");
        assertThat(responseDto.getMemberId()).isEqualTo(member.getId());
        assertThat(responseDto.getCreatedAt()).isEqualTo(findTask.getCreatedAt());
        assertThat(responseDto.getUpdatedAt()).isEqualTo(findTask.getUpdatedAt());


        System.out.println("responseDto.getId() = " + responseDto.getId());
        System.out.println("responseDto.getTitle() = " + responseDto.getTitle());
        System.out.println("responseDto.getContent() = " + responseDto.getContent());
        System.out.println("responseDto.getMemberId() = " + responseDto.getMemberId());
        System.out.println("responseDto.getCreatedAt() = " + responseDto.getCreatedAt());
        System.out.println("responseDto.getUpdatedAt() = " + responseDto.getUpdatedAt());
    }

    @Test
    @DisplayName("Task 수정 테스트")
    public void updateTaskTest() throws Exception {
        //given
        //1. member 생성
        Member member = new Member("member", "password", "email@example.com", "010-1111-1111");
        em.persist(member); em.flush();
        Long taskId = taskService.createTask(new TaskRequestDto("title", "content", member));

        //2. requestDto 생성
        TaskRequestDto requestDto = new TaskRequestDto(taskId, "new title", "new content");

        //when
        //3. task Information 불러오기
        TaskResponseDto responseDto = taskService.updateTask(requestDto); // task update
        Task findTask = taskRepository.findById(responseDto.getId()).get(); // 업데이트 된 task 객체

        //then
        assertThat(responseDto.getId()).isEqualTo(taskId);
        assertThat(responseDto.getTitle()).isEqualTo("new title");
        assertThat(responseDto.getContent()).isEqualTo("new content");
        assertThat(responseDto.getMemberId()).isEqualTo(member.getId());
        assertThat(responseDto.getCreatedAt()).isEqualTo(findTask.getCreatedAt());
        assertThat(responseDto.getUpdatedAt()).isEqualTo(findTask.getUpdatedAt());
    }

    @Test
    @DisplayName("Task 삭제 테스트")
    public void deleteTaskTest() throws Exception {
        //given
        //1. member 생성
        Member member = new Member("member", "password", "email@example.com", "010-1111-1111");
        em.persist(member); em.flush();
        Long taskId = taskService.createTask(new TaskRequestDto("title", "content", member));

        //2. requestDto 생성
        TaskRequestDto requestDto = new TaskRequestDto(taskId);
        em.flush(); em.clear();

        //when
        Long deleteTaskId = taskService.deleteTask(requestDto);

        //then
        assertThat(deleteTaskId).isEqualTo(taskId);
        Optional<Task> deletedTask = taskRepository.findById(deleteTaskId);
        assertThat(deletedTask).isEmpty();

    }

}