package project.task_app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.task_app.dto.MemberRequestDto;
import project.task_app.dto.TaskRequestDto;
import project.task_app.dto.TaskResponseDto;
import project.task_app.entity.Member;
import project.task_app.entity.Task;
import project.task_app.service.MemberService;
import project.task_app.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/tasks")
@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final MemberService memberService;


    /**
     * Task List (페이징, 검색 조건)
     * @param pageable : 페이징 조건
     * @param keyword : 검색 조건
     * @param isChecked : 달성 여부 (체크 여부)
     */
    @GetMapping("")
    public String home(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "isChecked", required = false) String isChecked,
                       Model model) {

        //task create form 넘기기
        model.addAttribute("taskForm", new TaskRequestDto());

        //현재 로그인된 username (Header 표시용)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("signedMember", authentication.getName());

        //현재 로그인된 member
        Member member = memberService.findByUsername(authentication.getName());


        Page<Task> tasks;
        /** 검색 조건 설정 */
        if (keyword != null && !keyword.isEmpty()) {
            /* searchKeyword X , isChecked : "true" */
            if ("checked".equals(isChecked)) {
                tasks = taskService.getIsCheckedTaskList(member, true, pageable);
            }
            /* searchKeyword X , isChecked : "false" */
            else if ("unchecked".equals(isChecked)) {
                tasks = taskService.getIsCheckedTaskList(member, false, pageable);
            }
            /* searchKeyword X */
            else {
                tasks = taskService.taskList(member, pageable);
            }
        } else {
            /* searchKeyword O , isChecked : "true" */
            if ("checked".equals(isChecked)) {
                tasks = taskService.searchIsChecked(member, true, keyword, pageable);
            }
            /* searchKeyword O , isChecked : "false" */
            else if ("unchecked".equals(isChecked)) {
                tasks = taskService.searchIsChecked(member, false, keyword, pageable);
            }
            /* searchKeyword O */
            else {
                tasks = taskService.search(member, keyword, pageable);
            }
        }
        Page<TaskResponseDto> list = tasks.map(TaskResponseDto::new);


        //task dto list
        model.addAttribute("tasks", list);
        model.addAttribute("keyword", keyword); //검색 키워드
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber()); //이전 페이지 정보
        model.addAttribute("next", pageable.next().getPageNumber()); //다음 페이지 정보
        model.addAttribute("hasPrevious", list.hasPrevious()); //이전 페이지 존재 여부
        model.addAttribute("hasNext", list.hasNext()); //다음 페이지 존재 여부


        //페이지 번호
        /** 페이지 블록 계산
         * currentPage = 5
         * User : 5 , Spring : 4
         * startPage = 1
         * endPage = 5
         * <= 1 2 3 4 5 =>
         */
        int currentPage = pageable.getPageNumber() + 1; //현재 페이지 정보(User side)
        model.addAttribute("current", currentPage);

        int blockSize = 5;
        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1; //블럭 시작 페이지
        int endPage = Math.min(startPage + blockSize - 1, list.getTotalPages()); //블럭 마지막 페이지

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        // 달성 Task / 전체 Task
        model.addAttribute("checkTaskNum", taskService.checkTaskNum(member, true));
        model.addAttribute("totalTaskNum", taskService.totalTaskNum(member));


        return "tasks/task-list";
    }

    //task 생성
    @PostMapping("")
    public String createTask(@ModelAttribute("taskForm") @Validated(TaskRequestDto.Create.class) TaskRequestDto dto,
                             BindingResult result, Model model) {

        //유효성 검사 오류 시, 에러 처리 로직
        if (result.hasErrors()) {
            //에러 메시지 반환
            List<String> errorMessage = result.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errorMessage", errorMessage);
            return "tasks/task-list";
        }

        //작성자 설정
        Member member = memberService.findByUsername(memberService.getCurrentUsername());
        dto.setMember(member);

        taskService.createTask(dto);

        return "redirect:/tasks";
    }

    /**
     * task 상세 정보
     */
    @GetMapping("/{taskId}")
    public String taskInfo(@PathVariable Long taskId, Model model) {
        TaskResponseDto responseDto = taskService.getTaskInformation(new TaskRequestDto(taskId));

        model.addAttribute("task", responseDto);


        model.addAttribute("signedMember", memberService.getCurrentUsername());

        return "tasks/task-info";
    }


    //task update
    @GetMapping("/{taskId}/update")
    public String taskUpdateForm(@PathVariable Long taskId, Model model) {

        //1. 수정대상 task
        Task task = taskService.getTask(taskId);

        //기존 내용 보내기
        model.addAttribute("task", new TaskRequestDto(taskId, task.getTitle(), task.getContent()));


        model.addAttribute("signedMember", memberService.getCurrentUsername());

        return "tasks/task-update";
    }

    @PostMapping("/{taskId}/update")
    public String taskUpdate(@PathVariable Long taskId,
                             @ModelAttribute("taskForm") @Validated(TaskRequestDto.Create.class) TaskRequestDto dto,
                             BindingResult result, Model model) {

        //유효성 검사 오류 시, 에러 처리 로직
        if (result.hasErrors()) {
            //에러 메시지 반환
            List<String> errorMessage = result.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errorMessage", errorMessage);
            return "tasks/task-update";
        }

        taskService.updateTask(dto);

        return "redirect:/tasks";
    }

    /**
     * task 체크 요청
     * isChecked == false -> isChecked == true
     * isChecked == true -> isChecked == false
     */
    @PostMapping("/{taskId}/checking")
    public String taskChecking(@PathVariable Long taskId, HttpServletRequest request) {
        taskService.taskCheck(taskId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }



    /**
     * Task 삭제
     */
    @PostMapping("/{taskId}/delete")
    public String deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(new TaskRequestDto(taskId));
        return "redirect:/tasks";
    }
}
