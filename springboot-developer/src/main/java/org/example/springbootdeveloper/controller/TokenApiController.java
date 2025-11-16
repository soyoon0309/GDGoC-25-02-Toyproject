package org.example.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.dto.CreateAccessTokenRequest;
import org.example.springbootdeveloper.dto.CreateAccessTokenResponse;
import org.example.springbootdeveloper.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// /api/token POST 요청이 오면 refresh token을 기반으로 새로운 access token을 만들어줌
@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    //clinet가 refresh token을 서버고 전송해서 새로운 access token을 생성해서 post
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}