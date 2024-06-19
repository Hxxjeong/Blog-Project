package com.example.blogproject.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // 전역 예외 처리
    @ExceptionHandler(Exception.class)
    public String handler(Exception e, Model model) {
        log.error("error: {}", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    // 유저를 찾을 수 없을 때 예외 처리
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
