package project.task_app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.task_app.oauth.CustomOAuth2UserService;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Autowired private CustomUserDetailsServiceAuthorities customUserDetailsServiceAuthorities;

    /*OAuth*/
    @Autowired private CustomOAuth2UserService customOAuth2UserService;



    // PasswordEncoder Bean 등록 - password 암호화 (방식 - BCryptPasswordEncoder)
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // WebSecurityCustomizer Bean 등록 - 정적 resources 접근을 위함
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스가 위치한 파일의 보안 처리를 무시 (누구든 접근 가능)
        return (web -> web.ignoring()
                .requestMatchers("/img/**", "/css/**", "/css/styles.css")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/", "/members/login", "/members/join").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin((form) ->
                        form
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginPage("/members/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/tasks", true)
                                .permitAll()
                )
                .userDetailsService(customUserDetailsServiceAuthorities)
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") //로그아웃 처리 URL
                                .logoutSuccessUrl("/") //로그아웃 성공 후 리다이렉트 할 URL
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                )
                .csrf(csrf ->
                        csrf
                                .ignoringRequestMatchers("/api/**") // /api/** 경로에 대한 CSRF 보호를 비활성화
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)))
        ;

        return http.build();
    }


}
