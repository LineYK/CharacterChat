package com.lineyk.characterchat.global.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path rootLocation;

    public LocalFileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패: " + rootLocation, e);
        }
    }
 
    @Override
    public String upload(MultipartFile file, String directory) {
        try {
            Path targetDir = rootLocation.resolve(directory);
            Files.createDirectories(targetDir);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFilename = UUID.randomUUID() + extension;

            Path targetPath = targetDir.resolve(storedFilename);
            file.transferTo(targetPath.toFile());

            log.info("파일 업로드 성공: {}", targetPath);
            return "/uploads/" + directory + "/" + storedFilename;
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }   
    }

    @Override
    public void delete(String fileUrl) {
        try {
            String relativePath = fileUrl.replaceFirst("/uploads/", "");
            Path filePath = rootLocation.resolve(relativePath);
            Files.deleteIfExists(filePath);
            log.info("파일 삭제 성공: {}", filePath);
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", fileUrl, e);
        }
    }
    
}
