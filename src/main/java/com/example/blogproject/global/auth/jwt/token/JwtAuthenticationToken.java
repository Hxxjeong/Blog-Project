//package com.example.blogproject.global.auth.jwt.token;
//
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//
//public class JwtAuthenticationToken extends AbstractAuthenticationToken {
//    private String token;
//    private Object principal;   // 로그인한 사용자 id, email
//    private Object credentials;
//
//    // security로 인증 이용
//    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
//                                  Object principal,
//                                  Object credentials) {
//        super(authorities);
//        this.principal = principal;
//        this.credentials = credentials;
//        this.setAuthenticated(true);
//    }
//
//    // token으로 인증 이용
//    public JwtAuthenticationToken(String token) {
//        super(null);
//        this.token = token;
//        this.setAuthenticated(false);
//    }
//
//    @Override
//    public Object getCredentials() {
//        return this.credentials;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return this.principal;
//    }
//}