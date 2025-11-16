package org.example.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.config.jwt.TokenProvider;
import org.example.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

//전달받은 리프레스 토큰의 유효성 검사를 진행하고, 유효한 경우 해당 리프레시 토큰으로 사용자 ID를 찾는다
//사용자 ID로 사용자를 찾은 후에는 토큰 제공자의 generateToken() 매세더를 호출해서 새로운 액세스 토큰 생성
@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        //전달 받은 refresh 유효성 검사
        //유효성 검사에 실패하면 예외 발생

            if (!tokenProvider.validToken(refreshToken)) {
                throw new IllegalArgumentException("Invalid refresh token");

            }
            Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
            User user = userService.findById(userId);
            return tokenProvider.generateToken(user, Duration.ofHours(2));
        }


}
