package com.example.s3practice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Uploader uploader;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestPart("image") MultipartFile multipartFile) {
        String uploadImagePath = uploader.uploadImage(multipartFile);
        return ResponseEntity.ok(uploadImagePath);
    }
}
