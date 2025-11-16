package org.example.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;
@EnableJpaAuditing //create_at, updated_at 자동 업데이트
@SpringBootApplication

public class SpringBootDeveloperApplication {
    public static void main(String[] args) {

        // .env 파일 로드
        Dotenv dotenv = Dotenv.load();
        System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
        System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}
