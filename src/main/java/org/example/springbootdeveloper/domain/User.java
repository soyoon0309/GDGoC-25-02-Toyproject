package org.example.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name="users")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Entity


//UserDetails는 스프링 시큐리티에서 사용자 인증 정보를 담아두는 인터페이스(구현해야 할 매소드들 여러개 존재)
public class User implements UserDetails { //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable = false)
    private Long id;

    @Column(name="email",updatable = false)
    private String email;

    @Column(name="password")
    private String password;


    //사용자 이름
    @Column(name="nickname", unique=true)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;

    }


    //UserDetails 인터페이스를 구현할 때 가져올 필수 오버라이드 메서드 들
    //사용자가 가지고 있는 권한의 목록을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));


    }

    //사용자의 id를 반환
    @Override
    public String getUsername() {
        return email;
    }
    //사용자의 패스워드 반환
    @Override
    public String getPassword() {
        return password;
    }

    //계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        //만료되었는지 확인하는 로직
        return true;
    }
    //계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true;
    }
    //사용자 이름 변경
    public User update(String nickname){
        this.nickname = nickname;
        return this;
    }



}
