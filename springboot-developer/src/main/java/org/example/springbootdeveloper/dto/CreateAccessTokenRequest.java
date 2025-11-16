package org.example.springbootdeveloper.dto;

import lombok.Getter;
import lombok.Setter;

//clinet가 server에게 보내는 요청 데이터를 담는 공간

@Getter
@Setter
public class CreateAccessTokenRequest { //새로운 엑세스 토큰을 만들어달라는 요청
    private String refreshToken; //이 요청을 하기 위해 클라이언트가 제출해야 하는 것(server가 이 데이터를 가지고 새로운 엑세스 토큰을 만듦)
}
