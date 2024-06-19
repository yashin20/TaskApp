package project.task_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.task_app.dto.MemberRequestDto;
import project.task_app.dto.MemberResponseDto;
import project.task_app.entity.Member;
import project.task_app.exception.DataAlreadyExistsException;
import project.task_app.exception.DataNotFoundException;
import project.task_app.exception.PasswordCheckFailedException;
import project.task_app.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //비밀번호 암호화
    public void passwordEncoding(MemberRequestDto dto) {
        String encoded = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encoded);
    }


    //Create Member(회원 생성)
    @Transactional
    public Long createMember(MemberRequestDto requestDto) {

        //0. 중복 검사 (username, email)
        duplicateValidation(requestDto);

        //0. 비밀번호 이중 검사
        passwordDoubleCheck(requestDto);

        //0. 비밀번호 암호화
        passwordEncoding(requestDto);

        //1. create member
        Member entity = requestDto.toEntity();

        //2. createdAt, updatedAt setting


        //3. save member
        Member createdMember = memberRepository.save(entity);
        return createdMember.getId();

    }

    //중복 검사
    public void duplicateValidation(MemberRequestDto requestDto) {

        //username 중복 확인
        if (memberRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new DataAlreadyExistsException("이미 존재하는 username 입니다.");
        }

        //email 중복 확인
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new DataAlreadyExistsException("이미 존재하는 E-mail 입니다.");
        }
    }

    //비밀번호 이중 검사
    public void passwordDoubleCheck(MemberRequestDto requestDto) {

        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())) {
            throw new PasswordCheckFailedException("비밀번호가 동일하지 않습니다.");
        }
    }



    //Read Member(회원 정보 확인)
    public MemberResponseDto getMemberInformation(MemberRequestDto requestDto) {

        //1. member 찾기
        Member member = memberRepository.findById(requestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("MemberService.getMemberInformation : 존재하지 않는 회원 입니다."));

        //2. member -> MemberResponseDto
        return new MemberResponseDto(member);
    }


    //Update Member(회원 정보 수정)
    @Transactional
    public MemberResponseDto updateMember(MemberRequestDto requestDto) {

        //1. member 찾기
        Member member = memberRepository.findById(requestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("MemberService.updateMember - 존재하지 않는 회원 입니다."));

        //비밀번호 수정 시, password == passwordCheck 확인
        if (requestDto.getPassword() != null && requestDto.getPasswordCheck() != null &&
                !requestDto.getPassword().isEmpty() && !requestDto.getPasswordCheck().isEmpty()) {
            passwordDoubleCheck(requestDto);


            // 비밀번호 암호화
            passwordEncoding(requestDto);
        }


        //2. update member
        member.update(requestDto);

        //3. member -> MemberResponseDto
        return new MemberResponseDto(member);
    }


    //Delete Member(회원 삭제)
    @Transactional
    public Long deleteMember(MemberRequestDto requestDto) {

        //1. member 찾기
        Member member = memberRepository.findById(requestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 회원 입니다."));

        //2. delete member
        memberRepository.delete(member);

        //3. Return delete member ID
        return requestDto.getId();
    }


    //Find By Username
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("MemberService.findByUsername - 존재하지 않는 회원 입니다."));
    }


    //로그인 인증 로직
    public boolean authenticate(String username, String password) {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isPresent()) {
            return passwordEncoder.matches(password, findMember.get().getPassword());
        }
        return false;
    }

    /* 현재 로그인 회원 Username*/
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}