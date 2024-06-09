package project.task_app.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.task_app.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    public interface Create {}
    public interface UpdateEmailPhone {}
    public interface UpdatePassword {}
    public interface IdOnly {}

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

    @NotNull(groups = {UpdateEmailPhone.class, UpdatePassword.class, IdOnly.class}, message = "ID는 필수 입력 값입니다.")
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", groups = Create.class,
            message = "아이디는 영문 대소문자, 숫자로 이루어진 4~20자리여야 합니다.")
    @NotBlank(groups = Create.class, message = "아이디는 필수 입력 값입니다.")
    private String username;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", groups = {Create.class, UpdatePassword.class},
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @NotBlank(groups = {Create.class, UpdatePassword.class}, message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", groups = {Create.class, UpdatePassword.class},
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @NotBlank(groups = {Create.class, UpdatePassword.class}, message = "비밀번호는 필수 입력 값입니다.")
    private String passwordCheck;

    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", groups = {Create.class, UpdateEmailPhone.class},
            message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(groups = {Create.class, UpdateEmailPhone.class}, message = "이메일은 필수 입력 값입니다.")
    private String email;

    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", groups = {Create.class, UpdateEmailPhone.class},
            message = "전화번호 형식이 올바르지 않습니다.")
    @NotBlank(groups = {Create.class, UpdateEmailPhone.class}, message = "전화번호는 필수 입력 값입니다.")
    private String phone;



    //등록 요청
    public MemberRequestDto(String username, String password ,String passwordCheck ,String email, String phone) {
        this.username = username;
        this.password = password;
        this.passwordCheck = passwordCheck;
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



    // update Form
    /**
     * 수정 가능 필드
     * @param email
     * @param phone
     */
    public MemberRequestDto(Long id, String email, String phone) {
        this.id = id;
        this.email = email;
        this.phone = phone;
    }
}
