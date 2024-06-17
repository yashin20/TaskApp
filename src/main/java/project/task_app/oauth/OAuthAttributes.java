package project.task_app.oauth;

import lombok.*;
import project.task_app.entity.Member;
import project.task_app.entity.UserRole;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String username;
    private String email;
    private UserRole role;

    //인증자가 누구인지 구분 메서드
    public static OAuthAttributes of(String registrationId,
                                     String usernameAttributeName,
                                     Map<String, Object> attributes) {
        //1. Google
        if (registrationId.equals("google")) {
            return ofGoogle(usernameAttributeName, attributes);
        }

        //2. Kakao
        if (registrationId.equals("kakao")) {
            return ofKakao(usernameAttributeName, attributes);
        }

        //3. Naver
        if (registrationId.equals("naver")) {
            return ofNaver(usernameAttributeName, attributes);
        }

        return null;
    }

    private static OAuthAttributes ofKakao(String usernameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");

        return OAuthAttributes.builder()
                .username((String) response.get("email"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String usernameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .username((String) response.get("email"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    private static OAuthAttributes ofGoogle(String usernameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .username((String) attributes.get("email"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .username(email)
                .email(email)
                .userRole(UserRole.SOCIAL)
                .build();
    }
}
