package com.example.s3practice;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    @Value("${ncp.s3.bucket}")
    private String bucket;

    private final AmazonS3 s3Client;
    private final FileNameGenerator fileNameGenerator;

    public String uploadImage(MultipartFile multipartFile) {
        String storePath = "test/";
        return upload(multipartFile, storePath);
    }

    private String upload(MultipartFile multipartFile, String storePath) {
        String fileName = fileNameGenerator.generateFileName(multipartFile, storePath);
        File file = convertToFile(multipartFile);

        try {
            return putImageToS3(file, fileName);
        } catch (SdkClientException e) {
            throw new RuntimeException("S3 저장에 실패했습니다.");
        } finally {
            removeFile(file);
        }
    }

    private File convertToFile(MultipartFile multipartFile) {
        File file = new File("/Users/jinho/Desktop/jinho/study/spring/s3-practice/src/main/resources/static/image/" + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            if (file.exists()) {
                throw new RuntimeException("MultipartFile에서 File로 데이터 전송에 실패했습니다.");
            }
            throw new RuntimeException("File 생성에 실패했습니다.");
        }
    }

    private String putImageToS3(File file, String fileName) {
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private void removeFile(File file) {
        if (file.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
}
