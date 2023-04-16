package com.example.s3practice;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class FileNameGenerator {

    public String generateFileName(MultipartFile multipartFile, String storePath) {
        String ext = extractExt(multipartFile);
        String newImageName = getNewImageName(ext);
        return storePath + newImageName;
    }

    private String extractExt(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (!StringUtils.hasText(contentType)) {
            throw new RuntimeException("이미지 확장자가 필요합니다.");
        }
        return contentType.substring(contentType.lastIndexOf("/") + 1);
    }

    private String getNewImageName(String ext) {
        return UUID.randomUUID() + "." + ext;
    }
}
