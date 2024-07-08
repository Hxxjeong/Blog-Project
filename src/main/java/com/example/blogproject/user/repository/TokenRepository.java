package com.example.blogproject.user.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenRepository {  // 발급받은 토큰 관리
    private Map<String, String> tokens = new ConcurrentHashMap<>();

    public void saveToken(String token, String username) {
        tokens.put(token, username);
    }

    public Optional<String> findUsernameByToken(String token) {
        return Optional.ofNullable(tokens.get(token));
    }

    public void deleteToken(String token) {
        tokens.remove(token);
    }
}