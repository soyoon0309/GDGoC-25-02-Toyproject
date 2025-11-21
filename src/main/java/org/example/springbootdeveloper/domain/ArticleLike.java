package org.example.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

//좋아요에 대한
@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class ArticleLike {
    //좋아요 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_id",updatable = false)
    private Long like_id;

    //Article과 User를 fk 로 연결

    //article_id가 fk
    //한 article은 여러 좋아요를 받을 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="article_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //게시물 삭제시 좋아요도 삭제
    private Article article;

    //한 User는 여러 좋아요를 누를 수 있다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private User user;




    @Builder
    public ArticleLike(Article article, User user) {
        this.article=article;
        this.user=user;
    }

}
