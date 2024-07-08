//package com.example.blogproject.global.auth.jwt.exception;
//
//import com.google.gson.Gson;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.HashMap;
//
//@Component
//@Slf4j
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {   // 예외 처리
//    // 사용자가 인증되지 않았을 때 실행
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException) throws IOException, ServletException {
//        String exception = (String) request.getAttribute("exception");  // 예외 발생 확인
//
//        // 어떤 요청인지 확인 (RestController / Controller)
//        if(isRestRequest(request))
//            handleRestResponse(request, response, exception);
//        else
//            handlePageResponse(request, response, exception);
//    }
//
//    // REST 요청인지 확인
//    private boolean isRestRequest(HttpServletRequest request) {
//        String requestedWithHeader = request.getHeader("X-Requested-With");
//        return "XMLHttpRequest".equals(requestedWithHeader) // JS에서 사용
//                || request.getRequestURI().startsWith("/api/");
//    }
//
//    // Rest 요청일 때 예외 처리
//    private void handleRestResponse(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    String exception) throws IOException {
//        // 어떤 오류인지 확인
//        log.error("Rest Request - Commence Get Exception : {}", exception);
//
//        // 에러 코드 확인 (enum 이용)
//        if (exception != null) {
//            if (exception.equals(JwtExceptionCode.INVALID_TOKEN.getCode())) {
//                log.error("entry point >> invalid token");
//                setResponse(response, JwtExceptionCode.INVALID_TOKEN);
//            }
//            else if (exception.equals(JwtExceptionCode.EXPIRED_TOKEN.getCode())) {
//                log.error("entry point >> expired token");
//                setResponse(response, JwtExceptionCode.EXPIRED_TOKEN);
//            }
//            else if (exception.equals(JwtExceptionCode.UNSUPPORTED_TOKEN.getCode())) {
//                log.error("entry point >> unsupported token");
//                setResponse(response, JwtExceptionCode.UNSUPPORTED_TOKEN);
//            }
//            else if (exception.equals(JwtExceptionCode.NOT_FOUND_TOKEN.getCode())) {
//                log.error("entry point >> not found token");
//                setResponse(response, JwtExceptionCode.NOT_FOUND_TOKEN);
//            }
//            else setResponse(response, JwtExceptionCode.UNKNOWN_ERROR);
//        }
//        else setResponse(response, JwtExceptionCode.UNKNOWN_ERROR);
//    }
//
//    // 페이지 요청일 때 예외 처리
//    private void handlePageResponse(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    String exception) throws IOException {
//        log.error("Page Request - Commence Get Exception : {}", exception);
//
//        if (exception != null) {
//            // 추가적인 페이지 요청에 대한 예외 처리 로직
//        }
//
//        response.sendRedirect("/loginform");
//    }
//
//    private void setResponse(HttpServletResponse response,
//                             JwtExceptionCode exceptionCode) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        HashMap<String, Object> errorInfo = new HashMap<>();
//        errorInfo.put("message", exceptionCode.getMessage());
//        errorInfo.put("code", exceptionCode.getCode());
//
//        // JSON 형식의 응답
//        Gson gson = new Gson();
//        String responseJson = gson.toJson(errorInfo);
//        response.getWriter().print(responseJson);
//    }
//}