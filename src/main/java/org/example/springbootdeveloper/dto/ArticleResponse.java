package org.example.springbootdeveloper.dto;

import lombok.Getter;
import org.example.springbootdeveloper.domain.Article;

//블로그의 내용을 조회하기 위한 DTO
@Getter
public class ArticleResponse {
    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle(); //article에서 title과 content를 받아서 저장
        this.content = article.getContent();
    }
}
