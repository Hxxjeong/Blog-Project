//package com.example.blogproject.uploadfile.service;
//
//import com.example.blogproject.uploadfile.entity.ProfileImage;
//import com.example.blogproject.uploadfile.repository.ProfileImageRepository;
//import com.example.blogproject.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class ProfileImageService {
//    private final ProfileImageRepository profileImageRepository;
//
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @Value("${default.profile-image}")
//    private String defaultProfileImage;
//
//    @Transactional
//    public ProfileImage saveProfileImage(MultipartFile image, User user) throws IOException {
//        String originName = image.getOriginalFilename();
//        String storedName = UUID.randomUUID() + "_" + originName;
//
//        ProfileImage profileImage = ProfileImage.builder()
//                .originName(originName)
//                .storedName(storedName)
//                .user(user)
//                .build();
//
//        File destinationDir = new File(uploadDir);
//        if (!destinationDir.exists()) {
//            destinationDir.mkdirs();
//        }
//
//        File destination = new File(destinationDir, storedName);
//        image.transferTo(destination);
//
//        return profileImageRepository.save(profileImage);
//    }
//
//    @Transactional
//    public void updateProfileImage(User user, MultipartFile newImageFile) throws IOException {
//        ProfileImage profileImage = user.getProfileImage();
//
//        String originName = newImageFile.getOriginalFilename();
//        String storedName = UUID.randomUUID() + "_" + originName;
//
//        if (profileImage != null) {
//            String filePath = uploadDir + profileImage.getStoredName();
//
//            try {
//                Files.deleteIfExists(Paths.get(filePath));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            profileImage = ProfileImage.builder()
//                    .originName(originName)
//                    .storedName(storedName)
//                    .build();
//        }
//        else {
//            profileImage = saveProfileImage(newImageFile, user);
//            user.setProfileImage(profileImage);
//        }
//
//        File destinationDir = new File(uploadDir);
//        if (!destinationDir.exists()) destinationDir.mkdirs();
//
//        File destination = new File(destinationDir, storedName);
//        newImageFile.transferTo(destination);
//
//        profileImageRepository.save(profileImage);
//    }
//
//    @Transactional
//    public void deleteProfileImage(User user) {
//        ProfileImage profileImage = user.getProfileImage();
//        if (profileImage != null) {
//            String filePath = uploadDir + profileImage.getStoredName();
//
//            try {
//                Files.deleteIfExists(Paths.get(filePath));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            user.setProfileImage(null);
//            profileImageRepository.delete(profileImage);
//        }
//    }
//
//    @Transactional
//    public void setDefaultProfileImage(User user) throws IOException {
//        String originName = defaultProfileImage;
//        String storedName = UUID.randomUUID() + "_" + originName;
//
//        ProfileImage profileImage = ProfileImage.builder()
//                .originName(originName)
//                .storedName(storedName)
//                .user(user)
//                .build();
//
//        File destinationDir = new File(uploadDir);
//        if (!destinationDir.exists()) {
//            destinationDir.mkdirs();
//        }
//
//        File source = new File(defaultProfileImage);
//        File destination = new File(destinationDir, storedName);
//        Files.copy(source.toPath(), destination.toPath());
//
//        profileImageRepository.save(profileImage);
//    }
//}