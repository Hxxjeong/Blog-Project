package com.example.blogproject.uploadfile;

import com.example.blogproject.post.entity.Post;
import com.example.blogproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UploadFileService {
    private final UploadFileRepository uploadFileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public UploadFile saveImage(MultipartFile image, User user) throws IOException {
        String originName = image.getOriginalFilename();
        String storedName = UUID.randomUUID() + "_" + originName;

        UploadFile uploadFile = UploadFile.builder()
                .originName(originName)
                .storedName(storedName)
                .user(user)
                .build();

        File destinationDir = new File(uploadDir);
        // 디렉토리가 없다면 생성
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }
        File destination = new File(destinationDir, storedName);
        image.transferTo(destination);

        return uploadFile;
    }

    // 이미지 수정
    @Transactional
    public void updateImage(Post post, MultipartFile newImageFile) throws IOException {
        UploadFile image = post.getImage();

        String originName = newImageFile.getOriginalFilename();
        String storedName = UUID.randomUUID() + "_" + originName;

        // 기존 이미지 파일 삭제
        if (image != null) {
            String filePath = uploadDir + image.getStoredName();

            try {
                Files.deleteIfExists(Paths.get(filePath));
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            image.setOriginName(originName);
            image.setStoredName(storedName);
        }
        else {
            image = saveImage(newImageFile, post.getUser());
            post.setImage(image);
        }

        // 새로운 이미지 파일 저장
        File destinationDir = new File(uploadDir);
        if (!destinationDir.exists()) destinationDir.mkdirs();  // 디렉토리가 없으면 생성

        File destination = new File(destinationDir, storedName);
        newImageFile.transferTo(destination);

        uploadFileRepository.save(image);
    }

    // 이미지 삭제
    @Transactional
    public void deleteImage(Post post) {
        if (post.getImage() != null) {
            String filePath = uploadDir + post.getImage().getStoredName();
            try {
                Files.deleteIfExists(Paths.get(filePath));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            post.removeImage();
        }
    }
}
