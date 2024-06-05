package project.task_app.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.task_app.dto.MemberRequestDto;
import project.task_app.dto.MemberResponseDto;
import project.task_app.entity.Member;
import project.task_app.exception.DataNotFoundException;
import project.task_app.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class MemberServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private EntityManager em;

    @Test
    @DisplayName("Create Member Test")
    public void testCreateMember() throws Exception {
        //given
        MemberRequestDto dto = new MemberRequestDto("username", "password", "email@example.com", "010-1111-1111");
        Member member = dto.toEntity();

        //when
        Long memberId = memberService.createMember(dto);
        Member findMember = memberRepository.findById(memberId).get();

        //then
        assertNotNull(findMember);
        assertEquals("username", findMember.getUsername());
        assertEquals("password", findMember.getPassword());
        assertEquals("email@example.com", findMember.getEmail());
        assertEquals("010-1111-1111", findMember.getPhone());
    }


    @Test
    @DisplayName("Read Member(회원 정보 확인) 테스트")
    public void testReadMember() throws Exception {
        //given
        MemberRequestDto dto = new MemberRequestDto("username", "password", "email@example.com", "010-1111-1111");
        Member member = dto.toEntity(); //id가 없는 상태
        em.persist(member);
        em.flush(); //DB 저장 후 id 부여 받음
        Long findMemberId = member.getId();
        em.clear();

        //when
        // 조회 요청
        MemberRequestDto request = new MemberRequestDto(findMemberId);
        // 응답
        MemberResponseDto responseDto = memberService.getMemberInformation(request);


        Member findMember = memberRepository.findById(findMemberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 회원 입니다."));

        //then
        assertThat(responseDto.getId()).isEqualTo(findMember.getId());
        assertThat(responseDto.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(responseDto.getPassword()).isEqualTo(findMember.getPassword());
        assertThat(responseDto.getEmail()).isEqualTo(findMember.getEmail());
        assertThat(responseDto.getPhone()).isEqualTo(findMember.getPhone());
    }



    @Test
    @DisplayName("Update Member(회원 정보 수정) 테스트")
    public void updateMemberTest() throws Exception {
        //given

        //1. 회원 저장 + memberId 받아 놓기
        MemberRequestDto dto = new MemberRequestDto("username", "password",
                "email@example.com", "010-1111-1111");
        Member member = dto.toEntity();
        em.persist(member);
        em.flush(); //DB 저장 후 id 부여 받음
        Long memberId = member.getId();
        em.clear();

        //when

        //2. 회원 정보 수정
        MemberResponseDto responseDto = memberService.updateMember(new MemberRequestDto(memberId, "newPassword",
                "newEmail@example.com", "010-9999-9999"));

        //3. 수정된 회원 조회
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 회원 입니다."));

        //then
        assertThat(responseDto.getId()).isEqualTo(memberId);
        assertThat(responseDto.getUsername()).isEqualTo("username");
        assertThat(responseDto.getPassword()).isEqualTo("newPassword");
        assertThat(responseDto.getEmail()).isEqualTo("newEmail@example.com");
        assertThat(responseDto.getPhone()).isEqualTo("010-9999-9999");

        System.out.println("responseDto.getId() = " + responseDto.getId());
        System.out.println("responseDto.getUsername() = " + responseDto.getUsername());
        System.out.println("responseDto.getPassword() = " + responseDto.getPassword());
        System.out.println("responseDto.getEmail() = " + responseDto.getEmail());
        System.out.println("responseDto.getPhone() = " + responseDto.getPhone());
    }
    
    
    
    @Test
    @DisplayName("Delete Member(회원 삭제) 테스트")
    public void deleteMemberTest() throws Exception {
        // given
        MemberRequestDto createDto = new MemberRequestDto("username", "password", "email@example.com", "010-1111-1111");
        Member member = createDto.toEntity();
        em.persist(member);
        em.flush();
        Long memberId = member.getId();
        em.clear();

        // when
        MemberRequestDto deleteRequestDto = new MemberRequestDto(memberId);
        Long deletedMemberId = memberService.deleteMember(deleteRequestDto);

        // then
        assertThat(deletedMemberId).isEqualTo(memberId);
        Optional<Member> deletedMember = memberRepository.findById(memberId);
        assertThat(deletedMember).isEmpty(); // 회원이 삭제되었음을 확인
    }
}