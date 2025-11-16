package org.example.springbootdeveloper.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import org.example.springbootdeveloper.domain.User;



import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class TokenProvider {

    //application.yml에 저장한 issuer,secrectKey를 가져오는 객체(비밀키를 코드에 직접 적지 않기 위해 사용)
    private final JwtProperties jwtProperties;

    //사용자가 로그인에 성공하면 제일 먼저 호출되는 메서드
    //user에게 expired At 만큼 유효한 토큰을 만들어 줌
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    //1.JWT 토큰 생성 메서드
    //실제 토큰을 만드는 핵심 로직
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) //JWT 타입의 헤더
                .setIssuer(jwtProperties.getIssuer()) //토큰 발급자
                .setIssuedAt(now) //발급 시간
                .setExpiration(expiry) //이때까지 유효
                .setSubject(user.getEmail()) //토큰의 주인(사용자 이메일)
                .claim("id", user.getId()) //이메일 외에 사용자 ID정보도 추가로
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) //우리 서버만 아는 비밀키로 토큰에 서명(봉인)
                .compact(); //위 정보들을 조합해서 긴 문자열JWT로 만듦
    }

    //2.JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) //비밀값으로 복호화
                    .parseClaimsJws(token); //토큰을파싱

            return true; //비밀번호가 일치하고, 만료시간이 지나지 않았으면
        } catch (Exception e) { //복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }
    }


    //3.토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

       return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),"",authorities),token,authorities);
    }
//4.토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser() //클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}