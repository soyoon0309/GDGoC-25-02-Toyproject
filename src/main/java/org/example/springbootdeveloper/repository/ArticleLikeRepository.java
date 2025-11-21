package org.example.springbootdeveloper.repository;

import org.example.springbootdeveloper.domain.Article;
import org.example.springbootdeveloper.domain.ArticleLike;
import org.example.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//사용자가 해당 게시물에 좋아요를 눌렀는지 확인
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findByArticleAndUser(Article article, User user);
}
