package project.task_app.entity;

import jakarta.persistence.*;
import lombok.*;
import project.task_app.dto.MemberRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity{

    /**
     * member_id(PK)
     * username
     * password
     * email
     * phone
     * userRole
     */

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;

    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @OneToMany(mappedBy = "member")
    private List<Task> tasks = new ArrayList<>();


    public Member(Long id, String username, String password, String email, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public Member(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    //Update (수정일자 업데이트)
    public void update(MemberRequestDto dto) {
        if (dto.getPassword() != null) {
            this.password = dto.getPassword();
        }
        if (dto.getEmail() != null) {
            this.email = dto.getEmail();
        }
        if (dto.getPhone() != null) {
            this.phone = dto.getPhone();
        }
    }



    /*소셜 로그인시 이미 등록된 회원이라면 수정날짜만 업데이트하고
    * 기존 데이터는 그대로 보존하도록 예외처리*/
    public Member updateUpdateAt() {
        this.onPreUpdate();
        return this;
    }
}
