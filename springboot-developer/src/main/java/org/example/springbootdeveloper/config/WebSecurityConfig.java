/*
package org.example.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.repository.UserRepository;
import org.example.springbootdeveloper.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class WebSecurityConfig {

    private final UserDetailService userService;

    //1.스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return (web)->web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    //2. 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/user")

                        ).permitAll() //login,signup,user http 요청이 오면 누구나 인증, 인가 없이 접근할 수 있게
                        .anyRequest().authenticated()) //그외의 http 요청에 대해서는 별도의 인가는 필요없지만 인증은 필요
                //폼기반 로그인 설정
                .formLogin(formLogin->formLogin
                .loginPage("/login") //로그인 페이지의 경로
                        .defaultSuccessUrl("/articles") //로그인이 완료되었을 때 이동할 경로
                )
                .logout(logout-> logout //로그아웃 설정
                        .logoutSuccessUrl("/login") //로그인이 완료되었을 때 이동할 경로
                        .invalidateHttpSession(true) //로그아웃 이후에 세션을 전체 삭제할 지 여부
                )
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화
                .build();
    }

//인증 관리자 관련 설정
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,UserDetailsService userDetailsService)
    throws Exception{
    DaoAuthenticationProvider authProvider= new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService); //사용할 정보 서비스 설정
    authProvider.setPasswordEncoder(bCryptPasswordEncoder);
    return new ProviderManager(authProvider);

}

//패스워드 인코더로 사용할 빈 등록
@Bean
public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
    }
}
*/

//OAuth2와 JWT를 함께 사용하기 위해 다른 설정 사용
