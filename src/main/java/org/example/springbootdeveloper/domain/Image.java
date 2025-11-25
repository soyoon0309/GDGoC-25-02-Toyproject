package org.example.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "images")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Image {

    @Id //image_id(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", updatable = false)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String url;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(name = "upload_order", nullable = false)
    private Integer uploadOrder;

    //Article에 1:n
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  //게시물 삭제 시 사용된 img도 같이 삭제
    private Article article;    //FK

    @Builder
    public Image(Article article, String url, String originalFileName, Integer uploadOrder) {
        this.article = article;
        this.url = url;
        this.originalFileName = originalFileName;
        this.uploadOrder = uploadOrder;
    }

    public void updateUploadOrder(Integer uploadOrder) {    //upload 이미지 수정 시 순서 변경
        this.uploadOrder = uploadOrder;
    }
}
