package com.lineyk.characterchat.global.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /** 
     * 파일을 저장하고 접근 가능한 URL을 반환합니다.
     * @param file 업로드할 파일
     * @param directory 저장할 하위 디렉토리 경로
     * @return 저장된 파일의 접근 가능한 URL
     */
    String upload(MultipartFile file, String directory);

    /**
     * 파일을 삭제합니다.
     * @param fileUrl 삭제할 파일의 URL
     */
    void delete(String fileUrl);
}
