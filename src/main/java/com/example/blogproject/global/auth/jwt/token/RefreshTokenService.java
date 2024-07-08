//package com.example.blogproject.global.auth.jwt.token;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class RefreshTokenService {
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    // 토큰 저장
//    @Transactional
//    public RefreshToken create(RefreshToken refreshToken) {
//        return refreshTokenRepository.save(refreshToken);
//    }
//
//    // 토큰 찾기
//    public Optional<RefreshToken> findToken(String refreshToken) {
//        return refreshTokenRepository.findByValue(refreshToken);
//    }
//
//    // 토큰 삭제
//    public void delete(String refreshToken) {
//        // value를 기준으로 찾고 있는 경우면 삭제
//        refreshTokenRepository.findByValue(refreshToken)
//                .ifPresent(refreshTokenRepository::delete);
//    }
//}
