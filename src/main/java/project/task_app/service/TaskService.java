package project.task_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.task_app.dto.TaskRequestDto;
import project.task_app.dto.TaskResponseDto;
import project.task_app.entity.Task;
import project.task_app.exception.DataNotFoundException;
import project.task_app.repository.TaskRepository;

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

}
