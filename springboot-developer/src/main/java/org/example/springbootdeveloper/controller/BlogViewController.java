package org.example.springbootdeveloper.controller;


import org.example.springbootdeveloper.domain.Article;
import org.example.springbootdeveloper.dto.ArticleViewResponse;
import org.example.springbootdeveloper.service.BlogService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.dto.ArticleListViewResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    //서비스 계층 호출을 위한 의존성 주입
    private final BlogService blogService;

    @GetMapping("/articles") // articles GET요청을 처리
    public String getArticles(Model model) {
        //blogService에서 모든 글 데이터를 가져옴 => DTO로 변환해서 fronted에 전달
        List<ArticleListViewResponse> articles=blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();


        //model에 articles라는 이름으로 데이터 추가=>템플릿에서 articles로 접근 가능

        model.addAttribute("articles", articles);

        //articleList.html을 화면에
        return "articleList";

    }
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article=blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false)Long id,Model model) {
        if(id==null) {
            model.addAttribute("article", new ArticleViewResponse());
        }
        else{
            Article article=blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }
}
