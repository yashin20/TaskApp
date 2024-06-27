package project.task_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.task_app.dto.MemberRequestDto;
import project.task_app.dto.MemberResponseDto;
import project.task_app.dto.TaskResponseDto;
import project.task_app.entity.Member;
import project.task_app.entity.Task;
import project.task_app.exception.DataAlreadyExistsException;
import project.task_app.exception.PasswordCheckFailedException;
import project.task_app.service.MemberService;
import project.task_app.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Login
     */
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new MemberRequestDto());
        return "members/login";
    }

    /**
     * Join
     */
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new MemberRequestDto());
        return "members/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("joinForm") @Validated(MemberRequestDto.Create.class) MemberRequestDto dto,
                       BindingResult bindingResult, Model model) {

        //유효성 검사 오류 시, 에러 처리 로직
        if (bindingResult.hasErrors()) {
            /*//에러 메시지 반환
            List<String> errorMassage = bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errorMessage", errorMassage);*/

            model.addAttribute("bindingResult", bindingResult);

            return "members/join";
        }

        try {
            //회원가입
            memberService.createMember(dto);
        }
        // 중복 검사 오류 시, 에러 처리 로직
        catch (DataAlreadyExistsException | PasswordCheckFailedException ex) {
            bindingResult.reject("errorMessage", ex.getMessage());
            model.addAttribute("errorMessage", ex.getMessage());

            return "members/join";
        }

        return "redirect:/tasks";
    }


    //현재 로그인된 member 를 반환하는 함수
    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); //현재 로그인한 Member's username

        return memberService.findByUsername(currentUsername);
    }


    /**
     * member Information ("/members/info")
     */
    @GetMapping("/info")
    public String memberInformation(@RequestParam(value = "finishedPage", defaultValue = "0") int finishedPage,
                                    @RequestParam(value = "unfinishedPage", defaultValue = "0") int unfinishedPage,
                                    @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                    Model model) {
        /**
         * 회원 정보
         */
        Member currentMember = getCurrentMember();
        MemberResponseDto responseDto = new MemberResponseDto(currentMember);
        model.addAttribute("responseDto", responseDto);
        //현재 로그인된 Username - header 에 명시
        model.addAttribute("signedMember", memberService.getCurrentUsername());

        // Pageable 수정
        Pageable finishedPageable = PageRequest.of(finishedPage, pageable.getPageSize(), pageable.getSort());
        Pageable unfinishedPageable = PageRequest.of(unfinishedPage, pageable.getPageSize(), pageable.getSort());

        /**
         * finishedTaskList
         */
        Page<Task> tasks1 = taskService.getIsCheckedTaskList(currentMember, true, finishedPageable);
        Page<TaskResponseDto> finishedTaskList = tasks1.map(TaskResponseDto::new);
        model.addAttribute("finishedTaskList", finishedTaskList);
        model.addAttribute("previous1", finishedPageable.previousOrFirst().getPageNumber()); //이전 페이지 정보
        model.addAttribute("next1", finishedPageable.next().getPageNumber()); //다음 페이지 정보
        model.addAttribute("hasPrevious1", finishedTaskList.hasPrevious()); //이전 페이지 존재 여부
        model.addAttribute("hasNext1", finishedTaskList.hasNext()); //다음 페이지 존재 여부

        //페이지 번호
        int currentPage1 = finishedPageable.getPageNumber() + 1; //현재 페이지 정보(User side)
        model.addAttribute("current1", currentPage1);

        int blockSize1 = 5;
        int startPage1 = ((currentPage1 - 1) / blockSize1) * blockSize1 + 1; //블럭 시작 페이지
        int endPage1 = Math.min(startPage1 + blockSize1 - 1, finishedTaskList.getTotalPages()); //블럭 마지막 페이지

        model.addAttribute("startPage1", startPage1);
        model.addAttribute("endPage1", endPage1);


        /**
         * unfinishedTaskList
         */
        Page<Task> tasks2 = taskService.getIsCheckedTaskList(currentMember, false, unfinishedPageable);
        Page<TaskResponseDto> unfinishedTaskList = tasks2.map(TaskResponseDto::new);
        model.addAttribute("unfinishedTaskList", unfinishedTaskList);
        model.addAttribute("previous2", unfinishedPageable.previousOrFirst().getPageNumber()); //이전 페이지 정보
        model.addAttribute("next2", unfinishedPageable.next().getPageNumber()); //다음 페이지 정보
        model.addAttribute("hasPrevious2", unfinishedTaskList.hasPrevious()); //이전 페이지 존재 여부
        model.addAttribute("hasNext2", unfinishedTaskList.hasNext()); //다음 페이지 존재 여부

        //페이지 번호
        int currentPage2 = unfinishedPageable.getPageNumber() + 1; //현재 페이지 정보(User side)
        model.addAttribute("current2", currentPage2);

        int blockSize2 = 5;
        int startPage2 = ((currentPage2 - 1) / blockSize2) * blockSize2 + 1; //블럭 시작 페이지
        int endPage2 = Math.min(startPage2 + blockSize2 - 1, unfinishedTaskList.getTotalPages()); //블럭 마지막 페이지

        model.addAttribute("startPage2", startPage2);
        model.addAttribute("endPage2", endPage2);



        // 달성 Task 수 / 미달성 Task 수
        model.addAttribute("checkTaskNum", taskService.checkTaskNum(currentMember, true));
        model.addAttribute("uncheckTaskNum", taskService.checkTaskNum(currentMember, false));

        return "members/info";
    }

    /**
     * member Information update (email, phone)
     */
    @GetMapping("/info/update")
    public String infoUpdateForm(Model model) {
        Member currentMember = getCurrentMember();

        MemberResponseDto responseDto = new MemberResponseDto(currentMember);
        // username, createAt, updatedAt 을 보여주기 위함
        model.addAttribute("responseDto", responseDto);

        //responseDto -> requestDto
        // email , phone 수정가능
        MemberRequestDto requestDto = new MemberRequestDto(responseDto.getId(), responseDto.getEmail(), responseDto.getPhone());
        model.addAttribute("requestDto", requestDto);

        //현재 로그인된 Username
        model.addAttribute("signedMember", memberService.getCurrentUsername());

        return "members/info-update";
    }


    @PostMapping("/info/update")
    public String updateMemberInfo(@ModelAttribute("requestDto") @Validated(MemberRequestDto.UpdateEmailPhone.class) MemberRequestDto dto,
                                   BindingResult bindingResult, Model model) {
        // 현재 로그인된 회원 객체
        Member currentMember = getCurrentMember();

        //유효성 검사 오류 발생시
        if (bindingResult.hasErrors()) {
            //기존 정보 그대로 반환 (수정 불가 정보 : username, createdAt, updatedAt)
            MemberResponseDto responseDto = new MemberResponseDto(currentMember);
            model.addAttribute("responseDto", responseDto);

            //기존 정보 그대로 반환 (수정 가능 정보 : email, phone)
            MemberRequestDto requestDto = new MemberRequestDto(responseDto.getId(), responseDto.getEmail(), responseDto.getPhone());
            model.addAttribute("requestDto", requestDto);

            //에러 메시지 반환
            List<String> errorMassage = bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errorMessage", errorMassage);

            return "members/info-update"; // 유효성 검사 실패 시 다시 폼으로
        }

        try {
            //회원 정보 업데이트
            memberService.updateMember(dto);
        }

        // 중복 검사 오류 시, 에러 처리 로직
        catch (DataAlreadyExistsException | PasswordCheckFailedException ex) {
            bindingResult.reject("errorMessage", ex.getMessage());
            model.addAttribute("errorMessage", ex.getMessage());

            return "members/info-update";
        }

        return "redirect:/members/info";
    }


    /**
     * password update
     */
    @GetMapping("/pw-update")
    public String passwordUpdateForm(Model model) {

        Member currentMember = getCurrentMember();
        //id, currentPassword, new password, new password check
        MemberRequestDto requestDto = new MemberRequestDto(currentMember.getId(), "", "", "", Boolean.TRUE);
        model.addAttribute("requestDto", requestDto);

        //현재 로그인된 Username
        model.addAttribute("signedMember", memberService.getCurrentUsername());

        return "members/password-update";
    }

    @PostMapping("/pw-update")
    public String passwordUpdate(@ModelAttribute("requestDto") @Validated(MemberRequestDto.UpdatePassword.class) MemberRequestDto dto,
                                 BindingResult bindingResult, Model model) {


        // 현재 로그인된 회원 객체
        Member currentMember = getCurrentMember();

        //유효성 검사 오류 발생시
        if (bindingResult.hasErrors()) {

            //id, currentPassword, new password, new password check
            MemberRequestDto requestDto = new MemberRequestDto(currentMember.getId(), "", "", "", Boolean.TRUE);
            model.addAttribute("requestDto", requestDto);

            //에러 메시지 반환
            List<String> errorMassage = bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errorMessage", errorMassage);

            return "members/password-update"; // 유효성 검사 실패 시 다시 폼으로
        }

        //현재 비밀번호 검사
        if (memberService.authenticate(currentMember.getUsername(), dto.getPassword())) {

            //id, currentPassword, new password, new password check
            MemberRequestDto requestDto = new MemberRequestDto(currentMember.getId(), "", "", "", Boolean.TRUE);
            model.addAttribute("requestDto", requestDto);

            model.addAttribute("errorMessage", "현재 비밀번호가 알맞지 않습니다.");

            return "members/password-update";
        }

        try {
            //회원 정보 업데이트
            memberService.updateMember(dto);
        }

        // 중복 검사 오류 시, 에러 처리 로직
        catch (DataAlreadyExistsException | PasswordCheckFailedException ex) {
            bindingResult.reject("errorMessage", ex.getMessage());
            model.addAttribute("errorMessage", ex.getMessage());

            return "members/password-update";
        }

        return "redirect:/members/info";
    }
}