package project.task_app.dto;

import lombok.Data;
import project.task_app.entity.Member;

@Data
public class MemberResponseDto {

    /**
     * Member 응답 DTO
     * <p>
     * 등록 요청 - return : id
     * <p>
     * 조회 요청 - return : id, username, password, email, phone
     * <p>
     * 수정 요청 - return : id, username, password, email, phone
     * <p>
     * 삭제 요청 - return : id
     */

    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;


    //등록 / 삭제 요청 - return
    public MemberResponseDto(Long id) {
        this.id = id;
    }

    //조회 / 수정 요청 - return
    public MemberResponseDto(Member entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
    }
}
