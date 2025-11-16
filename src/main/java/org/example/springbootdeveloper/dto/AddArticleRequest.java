package org.example.springbootdeveloper.dto;

//controller에서 요청한 본문을 받을 객체

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.springbootdeveloper.domain.Article;

@NoArgsConstructor //기본 생성자 추가
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {
    private String title;
    private String content;

    //빌더 패턴을 사용해 DTO를 엔티티로 만들어 주는 메서드
    //추후 블로그에 글을 추가할 때 저장할 엔티티로 변환하는 용도
    public Article toEntity(String author) {
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
