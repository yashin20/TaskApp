package project.task_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.task_app.dto.TaskRequestDto;
import project.task_app.dto.TaskResponseDto;
import project.task_app.entity.Member;
import project.task_app.entity.Task;
import project.task_app.exception.DataNotFoundException;
import project.task_app.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    //Create Task(Task 생성)
    @Transactional
    public Long createTask(TaskRequestDto requestDto) {

        //1. create Task
        Task entity = requestDto.toEntity();

        //2. createdAt, updatedAt setting

        //3. save Task
        Task createdTask = taskRepository.save(entity);
        return createdTask.getId();
    }


    //Read Task(Task 조회)
    public TaskResponseDto getTaskInformation(TaskRequestDto requestDto) {

        //1. Task 찾기
        Task task = taskRepository.findById(requestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 Task 입니다."));

        //2. task -> taskResponseDto
        return new TaskResponseDto(task);
    }

    //task 조회
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 Task 입니다."));
    }


    //Update Task(Task 수정)
    @Transactional
    public TaskResponseDto updateTask(TaskRequestDto requestDto) {

        //1. Task 찾기
        Task task = taskRepository.findById(requestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 Task 입니다."));

        //2. update task
        task.update(requestDto);

        //3. task -> TaskResponseDto
        return new TaskResponseDto(task);
    }

    //Delete Task(Task 삭제)
    @Transactional
    public Long deleteTask(TaskRequestDto requestDto) {

        //1. Task 찾기
        Task task = taskRepository.findById(requestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 Task 입니다."));

        //2. delete task
        taskRepository.delete(task);

        //3. return delete task ID
        return requestDto.getId();
    }


    /**
     * Member 의 Task 검색
     */
    public Page<Task> taskList(Member member, Pageable pageable) {

        return taskRepository.findByMember(member, pageable);
    }

    /**
     * Task 체크여부 수정
     */
    @Transactional
    public void taskCheck(Long taskId) {
        //1. Task 찾기
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 Task 입니다."));

        task.updateIsChecked();
    }

    /**
     * finishedTaskList
     * - "isChecked == true" 인 TaskList
     * - "isChecked == false" 인 TaskList
     */
    public Page<Task> getIsCheckedTaskList(Member member, Boolean isChecked ,Pageable pageable) {
        return taskRepository.findByMemberAndIsChecked(member, isChecked, pageable);
    }

    //전체 과업 수
    public Integer totalTaskNum(Member member) {
        return taskRepository.findByMember(member).size();
    }

    //완료 과업 수
    public Integer checkTaskNum(Member member, Boolean isChecked) {
        return taskRepository.findByMemberAndIsChecked(member, isChecked).size();
    }

    /**
     * Task Title 검색
     */
    public Page<Task> search(Member member, String searchKeyword, Pageable pageable) {
        return taskRepository.findByMemberAndTitleContaining(member, searchKeyword, pageable);
    }

    /**
     * @return : Page<Task> && isChecked == (true, false)
     * @Param : Page<Task>
     */
    /*public Page<Task> pagingCheck(Page<Task> list, Boolean isChecked) {

    }*/


    /**
     * searchKeyword
     * isChecked
     * Member
     */
    public Page<Task> searchIsChecked(Member member, Boolean isChecked, String searchKeyword, Pageable pageable) {
        return taskRepository.findByMemberAndIsCheckedAndTitleContaining(member, isChecked, searchKeyword, pageable);
    }
}
