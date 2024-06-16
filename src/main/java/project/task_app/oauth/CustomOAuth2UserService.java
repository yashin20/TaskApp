package project.task_app.oauth;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.task_app.dto.MemberResponseDto;
import project.task_app.entity.Member;
import project.task_app.repository.MemberRepository;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 서비스 id 구분 코드 (구글, 카카오, 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행 시 키가 되는 필드 값 (PK) (구글의 기본 코드는 "sub")
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService - //인증자가 누구인지 구분 메서드
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());


        Member member = saveOrUpdate(attributes);

        // OAuth2User 객체로부터 UserDetails 객체 생성
        UserDetails userDetails = User.builder()
                .username(member.getUsername()) // 사용자 이름 또는 ID
                .password("") // 비밀번호 필드
                .roles(String.valueOf(member.getUserRole())) // 사용자 역할, "ROLE_" 접두사 없이 설정
                .build();

        // Spring Security가 인증 수행
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return new DefaultOAuth2User(
                userDetails.getAuthorities(),
                attributes.getAttributes(),
                "email");
    }

    /*소셜 로그인시,
    * 기존 회원 - 수정일자(updateAt)만 업데이트
    * 새로운 회원 - 회원(member) 저장*/
    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(Member::updateUpdateAt)
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }
}