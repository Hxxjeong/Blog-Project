//package com.example.blogproject.global.auth.jwt.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//import java.util.List;
//
//@Component
//@Slf4j
//public class JwtTokenizer {
//    private final byte[] accessSecret;
//    private final byte[] refreshSecret;
//
//    public static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; //30분
//    public static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; //7일
//
//    // yml 파일을 통해 key 설정 후 생성
//    public JwtTokenizer(@Value("${jwt.secretKey}") String accessSecret,
//                        @Value("${jwt.refreshKey}") String refreshSecret){
//        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);  // 바이트 배열로 변환
//        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
//    }
//
//    private String createToken(Long id, String email, String name, String username, List<String> roles,
//                               Long expire, byte[] secretKey){
//        // 기본으로 가지고 있는 claim: subject
//        Claims claims = Jwts.claims().setSubject(email);    // email 기준
//
//        //필요한 정보 저장
//        claims.put("username",username);
//        claims.put("name",name);
//        claims.put("userId",id);
//        claims.put("roles", roles);
//
//        // 토큰의 만료 시간과 사용할 보안 키 지정
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime() + expire))
//                .signWith(getSigningKey(secretKey))
//                .compact();
//    }
//
//    //ACCESS Token 생성
//    public String createAccessToken(Long id, String email, String name, String username, List<String> roles){
//        return createToken(id, email, name, username, roles, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret);
//    }
//    //Refresh Token 생성
//    public String createRefreshToken(Long id, String email, String name, String username, List<String> roles){
//        return createToken(id, email, name, username, roles, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret);
//    }
//
//    // 서명 키 생성
//    // secretKey: byte 형식
//    public static Key getSigningKey(byte[] secretKey){
//        return Keys.hmacShaKeyFor(secretKey);
//    }
//
//    // 토큰으로부터 userId 얻기 (클라이언트로부터 넘어왔을 떄)
//    public Long getUserIdFromToken(String token){
//        String[] tokenArr = token.split(" ");
//        token = tokenArr[1];
//        Claims claims = parseToken(token,accessSecret);
//        return Long.valueOf((Integer)claims.get("userId"));
//    }
//
//    // 토큰을 parsing하여 claims 추출
//    public Claims parseToken(String token, byte[] secretKey){
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey(secretKey))
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public Claims parseAccessToken(String accessToken) {
//        return parseToken(accessToken, accessSecret);
//    }
//
//    public Claims parseRefreshToken(String refreshToken) {
//        return parseToken(refreshToken, refreshSecret);
//    }
//}
