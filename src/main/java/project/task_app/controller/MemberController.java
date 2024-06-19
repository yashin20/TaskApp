package project.task_app.controller;

import lombok.RequiredArgsConstructor;
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
import project.task_app.entity.Member;
import project.task_app.exception.DataAlreadyExistsException;
import project.task_app.exception.PasswordCheckFailedException;
import project.task_app.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
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
            //에러 메시지 반환
            List<String> errorMassage = bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.toList());
            model.addAttribute("errorMessage", errorMassage);

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

        return "redirect:/";
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
    public String memberInformation(Model model) {
        Member currentMember = getCurrentMember();

        MemberResponseDto responseDto = new MemberResponseDto(currentMember);
        model.addAttribute("responseDto", responseDto);

        //현재 로그인된 Username
        model.addAttribute("signedMember", memberService.getCurrentUsername());

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