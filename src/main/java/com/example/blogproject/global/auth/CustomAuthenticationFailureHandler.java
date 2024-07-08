//package com.example.blogproject.global.auth;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String errorMessage = "Invalid Username or Password";
//
//        if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
//            errorMessage = "비밀번호가 올바르지 않습니다.";
//        } else if (exception.getMessage().equalsIgnoreCase("UserDetailsService returned null, which is an interface contract violation")) {
//            errorMessage = "존재하지 않는 아이디입니다.";
//        }
//
//        request.setAttribute("errorMessage", errorMessage);
//        request.getRequestDispatcher("/loginform?error").forward(request, response);
//    }
//}
