package project.task_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.task_app.dto.MemberRequestDto;
import project.task_app.dto.MemberResponseDto;
import project.task_app.entity.Member;
import project.task_app.exception.DataNotFoundException;
import project.task_app.repository.MemberRepository;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    //Create Member(회원 생성)
    @Transactional
    public Long createMember(MemberRequestDto requestDto) {

        //1. create member
        Member entity = requestDto.toEntity();

        //2. createdAt, updatedAt setting
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        //3. save member
        Member member = memberRepository.save(entity);
        return member.getId();

    }


    //Read Member(회원 정보 확인)
    public MemberResponseDto getMemberInformation(MemberRequestDto requestDto) {

        //1. member 찾기
        Member member = memberRepository.findById(requestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 회원 입니다."));

        //2. member -> MemberResponseDto
        return new MemberResponseDto(member);
    }


    //Update Member(회원 정보 수정)
    @Transactional
    public MemberResponseDto updateMember(MemberRequestDto requestDto) {

        //1. member 찾기
        Member member = memberRepository.findById(requestDto.getId())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 회원 입니다."));

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
}
