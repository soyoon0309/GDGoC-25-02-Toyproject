package org.example.springbootdeveloper.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
//stateless 서버가 OAuth 2.0 인증을 위해 리디이렉트(구글 로그인 페이지)를 보낼 때 원래 요청 정보를 잃러버리지 않도록 자바객체를 문자열로 변환하여 쿠키에 잠시 저장하고, 사용자가 돌아왔을 때 다시 객체로 복원하는 유틸리티

public class CookieUtil {
    //요청값(이름, 값, 만료기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    //쿠키의 이름을 입력받아 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for(Cookie cookie : cookies) {
            if(name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
    //객체를 직렬화해 쿠키의 값으로 변환
    //쿠키에는 문자열만 저장할 수 있다 => OAuth2AuthorizationRequest라는 복잡한 **'자바 객체(Object)'**를 string 으로 변경
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }
    //쿠키를 역직렬화해 객체로 변환
    //문자열을 객체로 복원( 저장된 쿠키를 가져와서 자바 객체로 되돌림)
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

}
