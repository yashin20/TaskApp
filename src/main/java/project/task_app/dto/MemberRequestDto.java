package project.task_app.dto;


import lombok.Data;
import project.task_app.entity.Member;

@Data
public class MemberRequestDto {

    /**
     * Member 요청 DTO
     * <p>
     * 등록 요청 : username, password, email, phone
     * <p>
     * 조회 요청 : id
     * <p>
     * 수정 요청 : id, password, email, phone
     * <p>
     * 삭제 요청 : id
     */

    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;

    //등록 요청
    public MemberRequestDto(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    //수정 요청
    public MemberRequestDto(Long id, String password, String email, String phone) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    //조회 / 삭제 요청
    public MemberRequestDto(Long id) {
        this.id = id;
    }


    //DTO -> Entity
    public Member toEntity() {
        return new Member(
                this.id,
                this.username,
                this.password,
                this.email,
                this.phone);
    }
}
