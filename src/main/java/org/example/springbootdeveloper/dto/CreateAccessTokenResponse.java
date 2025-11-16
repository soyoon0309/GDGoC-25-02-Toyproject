package org.example.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//server가 accessToken을 새로 발급받아서 clinet에게 전달해주기 위해서 dto에 accessToken을 담음
@AllArgsConstructor
//객체가 이미 완성된 상태로 dto에 전달되기에(이미 accessToken이 다 만들어짐)=>setter가 필요 없음
@Getter
public class CreateAccessTokenResponse {
    private String accessToken;
}
