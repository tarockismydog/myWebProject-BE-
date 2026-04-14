package mallapi.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct // 생성자 대신 초기화 시키고자 할 때 -> 폴더를 만들어주는 용도로 사용
    public void init() {
        File tempFolder =new File(uploadPath);
        
        if(!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        uploadPath = tempFolder.getAbsolutePath();
        log.info(uploadPath);

    }


    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {

        if(files == null || files.size() == 0) {
            return null;
        }

        List<String> uploadNames = new ArrayList<>();

        for (MultipartFile file : files) {

            if(file.getOriginalFilename() == null || file.getOriginalFilename().equals("")) {
                // 파일이 실제로 안 들어온 경우이므로 skip
                continue;
            }

            String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, savedName);
            // 문자열 경로보다 안전한 파일 경로 객체 (Paths.get("c:/upload", "a.jpg") 처럼 OS에 맞게 자동 변환됨)

            try {
                Files.copy(file.getInputStream(), savePath); // copy => 저장

                String contentsType = file.getContentType();
                if(contentsType != null && contentsType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "s_"+savedName);
                    Thumbnails.of(savePath.toFile()).size(200, 200).toFile(thumbnailPath.toFile());
                }

                uploadNames.add(savedName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        // saveFiles : 1. UUID형식의 이름 변환 2. Files.copy로 실제 파일 저장
        return uploadNames;
    }

    public ResponseEntity<Resource> getFile(String fileName) {
         /* 👉 서버에 있는 파일을 찾아서 HTTP 응답으로 내려주는 코드
                파일 경로 생성
                → 파일 존재 확인
                → 없으면 default 이미지로 대체
                → Content-Type 설정
                → ResponseEntity로 반환
        */

        // Resource란 파일을 감싸는 객체 (Spring에서 파일 응답용)
        // 실제 파일을 바로 쓰는 게 아니라 Resource로 감싸서 응답
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);

        if(!resource.isReadable()) { // 파일이 존재하고 읽을 수 있는지 확인
            resource = new FileSystemResource(uploadPath+File.separator+"default.png");
            // OS마다 경로 구분자(\, /)가 다르기 때문에 File.separator 사용
        }

        // 헤더설정 : HTTP 응답 헤더를 담는 객체
        HttpHeaders headers = new HttpHeaders();

        try {
            // 이 파일이 어떤 타입인지 브라우저에게 알려주기 위해 Content-Type를 설정
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
        /*
        ResponseEntity = {
            status (200 OK)
            headers (Content-Type 등)
            body (파일 데이터)
        }
        */
    }

    public void deleteFile(List<String> fileNames) {
        if(fileNames == null || fileNames.size() == 0) { return; }

        fileNames.forEach(fileName -> {
            String thumbnailFileName = "s_"+fileName;

            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName); 
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                // 파일 있으면 서버에 해당 이미지 파일 삭제
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (Exception e) {
                throw new RuntimeException();
            }

        });
    }
}