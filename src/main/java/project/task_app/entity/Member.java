package project.task_app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.task_app.dto.MemberRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity{

    /**
     * member_id(PK)
     * username
     * password
     * email
     * phone
     */

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;

    private String email;
    private String phone;

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
        if (dto.getUsername() != null) {
            this.username = dto.getUsername();
        }
        if (dto.getPassword() != null) {
            this.password = dto.getPassword();
        }
        if (dto.getEmail() != null) {
            this.email = dto.getEmail();
        }
        if (dto.getPhone() != null) {
            this.phone = dto.getPhone();
        }

        setUpdatedAt(LocalDateTime.now());
    }
}
