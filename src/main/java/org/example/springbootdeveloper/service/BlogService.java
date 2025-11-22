package org.example.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springbootdeveloper.domain.Article;
import org.example.springbootdeveloper.domain.ArticleLike;
import org.example.springbootdeveloper.domain.Image;
import org.example.springbootdeveloper.domain.User;
import org.example.springbootdeveloper.dto.AddArticleRequest;
import org.example.springbootdeveloper.dto.UpdateArticleRequest;
import org.example.springbootdeveloper.repository.ArticleLikeRepository;
import org.example.springbootdeveloper.repository.BlogRepository;
import org.example.springbootdeveloper.repository.ImageRepository;
import org.example.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor //빈을 생성자로 생성
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    //해당 블로그에 좋아요가 눌러져있는지 확인
    private final UserRepository userRepository;
    private final ArticleLikeRepository articleLikeRepository;

    //image 처리에 필요
    private final ImageFileService imageFileService;
    private final ImageRepository imageRepository;

    //블로그 글 추가 메서드
    //save()는 JpaRepository에서 지원하는 저장 메서드로
    //AddArticleRequest 클래스에 저장된 값들은 article 데이터베이스에 저장
    public Article save(AddArticleRequest request, String userName, List<MultipartFile> files) {
        //Article id 생성을 위해 미리 article 저장
        Article savedArticle =  blogRepository.save(request.toEntity(userName));
        //image file이 존재할 경우 처리
        if (files != null && !files.isEmpty()) {    //저장된 file이 하나도 없던 경우
            int order = 1;  //uploadOrder 초기값 1로 설정

            for (MultipartFile file : files) {
                //empty file일 경우 건너뛰기
                if (file.isEmpty())
                    continue;

                //empty가 아닌 경우
                String imgFileUrl = imageFileService.upload(file);

                //upload 실패 시 우선 null 처리
                if (imgFileUrl == null)
                    continue;

                //image Entity 생성
                Image image = Image.builder()
                        .article(savedArticle)
                        .url(imgFileUrl)
                        .originalFileName(file.getOriginalFilename())
                        .uploadOrder(order++)   //1 부터 시작해서 여러 개 저장할 때 마다 ++
                        .build();

                //image entity DB save
                imageRepository.save(image);
            }
        }
        return savedArticle;
    }

    //블로그의 내용을 불러오는 api 작성
    //article 테이블에 저장되어 있는 모든 데이터를 조회합니다.=> 요청을 받아 controller에 전달
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }



    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no found:" + id));
        authorizeArticleAuthor(article);
        //삭제하기 전 인증 객체에 담겨있는 사용자의 정보와 글을 작성한 사용자의 정보 비교
        blogRepository.delete(article);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no found:" + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());
        return article;
    }


    //게시물을 작성한 유저인지 확인
    private void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }

    }

    //좋아요 기능 구현
    @Transactional //DB 상태 변경(삽입/삭제/수정)이 여러 개 일어나므로
    public boolean toggleLike(Long articleId, String userEmail) {
        //1. 게시글 조회
        Article article=blogRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: id=" + articleId));
        //2. 사용자 조회
        User user=userRepository.findByEmail(userEmail)
                .orElseThrow(()->new IllegalArgumentException("유저를 찾을 수 없습니다. useremail="+userEmail));

        //3. 좋아요 여부 확인
        Optional<ArticleLike> likeOptional=articleLikeRepository.findByArticleAndUser(article, user);
        if (likeOptional.isPresent()) {
            //좋아요가 있다면 -> 삭제
            articleLikeRepository.delete(likeOptional.get());
            article.decreaseLikeCount(); //Article 엔티티에서 like_count 감소
            return false; //좋아요 취소
        }
        else{
            articleLikeRepository.save(ArticleLike.builder().
                    article(article).
                    user(user).
                    build());
            article.increaseLikeCount(); //Article 엔티티에서 like_count 증가
            return true;
        }
    }


}




