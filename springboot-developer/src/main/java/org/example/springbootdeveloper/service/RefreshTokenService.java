package org.example.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.domain.RefreshToken;
import org.example.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

//전달받은 refresh 토큰으로 리프레시 토큰 객체를 검색해서 전달
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected refreshToken"));
    }
}
