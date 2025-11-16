package org.example.springbootdeveloper.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.example.springbootdeveloper.config.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor

//token을 검사하는 필터
public class TokenAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFiler: 모든 HTTP 요청마다 단 한번만 이 필터가 실행되도록 보장
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        //http request 헤더 부분에 token이 포함되어서 서버에 요청되니 헤더 부분을 띄어서 token에 저장
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        //"Bearer"라는 접두사를 떼어내고, 순수한 토큰 문자열 xxxxx.yyyyy.zzzzz만 꺼냄
        String token = getAccessToken(authorizationHeader);

        //token이 유효하면
        if (tokenProvider.validToken(token)) {
            //인증 정보 생성
            //token이 유효한 것을 확인했으니 tokenProvider에게 이 정보를 바탕으로 스프링 시큐리티가 알아볼 수 있는 인증 정보 객체를 만들어 달라고 요청
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
