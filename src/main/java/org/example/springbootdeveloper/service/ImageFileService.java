package org.example.springbootdeveloper.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageFileService {

    // 프로젝트 루트 경로에 'imgfiles' 폴더를 생성해서 저장
    // System.getProperty("user.dir")은 현재 프로젝트의 절대 경로를 반환
    private final String fileDir = System.getProperty("user.dir") + "/imgfiles/";

    //MultipartFile -> spring에서 file upload를 처리
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            return null; // 파일이 없으면 null 반환
        }

        try {
            // 저장할 폴더가 없으면 생성
            File directory = new File(fileDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 원본 파일 이름 가져오기
            String originalFilename = file.getOriginalFilename();

            // 파일 이름 중복 방지를 위한 UUID(난수) 생성 (ex: uuid_originalName.jpg)
            // 다른 파일과 중복되어 파일 덮어쓰기 되는 것을 방지
            String savedFileName = UUID.randomUUID() + "_" + originalFilename;

            // 파일 저장 경로 설정
            String filePath = fileDir + savedFileName;

            // 실제로 로컬에 파일 저장
            file.transferTo(new File(filePath));

            // 저장된 경로(또는 파일명) 반환
            // 배포를 위해 AWS S3 사용 시 "https://s3..." URL을 반환하도록 수정 예정
            return "/upload/" + savedFileName;

        } catch (IOException e) {
            // 파일 저장 중 에러 발생 시 예외 처리
            e.printStackTrace();
            return null;
        }
    }
}