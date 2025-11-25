package org.example.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter //모든 필드에 대한 접근 메서드(get 메서드 자동 생성)
@NoArgsConstructor(access = AccessLevel.PROTECTED) //접근제어자가 protected인 기본 생성자 생성

public class Article {
    @Id //id 필드를 primary key로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키를 자동으로 1씩 증가
    @Column(name="id",updatable = false)
    private Long id;


    @Column(name="title",nullable = false)
    private String title;

    @Column(name="content",nullable = false)
    private String content;

    @Column(name="author",nullable = false)
    private String author;

    @CreatedDate //엔티티가 생성될 때 생성 시간 저장
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @LastModifiedDate //엔티티가 수정될 때 수정 시간 저장
    @Column(name="updated_at")
    private LocalDateTime updateAt;

    //like_count 필드 추가(좋아요 개수)
    @Column(name="like_count",nullable = false) 
    private Integer likeCount=0;  //좋아요 개수 0으로 초기화

    // mappedBy="article": Image 클래스의 'article' 필드가 관계의 주인임을 표시
    // CascadeType.ALL: 게시글 지우면 이미지도 삭제
    // orphanRemoval=true: 리스트에서 이미지를 제거하면 DB에서도 삭제
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>(); // NullPointerException 방지 초기화

    @Builder //빌더 패턴으로 객체 생성
    public Article(String author,String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.likeCount=0;
        //image list는 위에서 초기화 했으므로 추가 안해도 괜찮음
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;

    }
    //좋아요 수 1 감소
    public void decreaseLikeCount() {
        if(likeCount>0) {
            this.likeCount--;
        }
    }
    //좋아요 수 1 증가
    public void increaseLikeCount() {

        this.likeCount++;
    }
    // 이미지를 추가할 때, 양방향 관계를 안전하게 맺어주는 메서드
    public void addImage(Image image) {
        this.images.add(image);
    }
}