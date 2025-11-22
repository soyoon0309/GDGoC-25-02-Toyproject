package org.example.springbootdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.springbootdeveloper.domain.Article;
import org.example.springbootdeveloper.domain.Image;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String author;
    private Integer likeCount;
    private List<String> imageUrls;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt=article.getCreateAt();
        this.author=article.getAuthor();
        this.likeCount=article.getLikeCount();
        this.imageUrls = article.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }
}
