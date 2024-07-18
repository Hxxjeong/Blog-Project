package com.example.blogproject.user.controller;

import com.example.blogproject.blog.BlogService;
import com.example.blogproject.user.service.UserService;
import com.example.blogproject.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BlogService blogService;

    @GetMapping("/loginform")
    public String loginForm(HttpServletRequest request, Model model) {
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
        return "login";
    }

    @GetMapping("/userregform")
    public String userRegForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/welcome")
    public String welcome() { return "welcome"; }

    // 회원가입
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "register";

        boolean isUsernameDuplicated = false;
        boolean isEmailDuplicated = false;

        try {
            userService.findByUsername(user.getUsername());
            isUsernameDuplicated = true;
            bindingResult.rejectValue("username", "error.user", "이미 존재하는 id입니다.");
        } catch (NoSuchElementException e) {
            // 사용자 이름이 없는 경우 처리
        }

        try {
            userService.findUserByEmail(user.getEmail());
            isEmailDuplicated = true;
            bindingResult.rejectValue("email", "error.user", "이미 존재하는 이메일입니다.");
        } catch (NoSuchElementException e) {
            // 이메일이 없는 경우 처리
        }

        if (isUsernameDuplicated || isEmailDuplicated) {
            return "register";
        }

        userService.join(user);

        // 블로그 생성
        blogService.create(user.getId());

        return "redirect:/welcome";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {
        try {
            User user = userService.findByUsername(username);
            if (!userService.checkPassword(user, password)) {
                model.addAttribute("error", "비밀번호가 올바르지 않습니다.");
                return "login";
            }

            String token = UUID.randomUUID().toString();
            userService.saveToken(token, username);

            Cookie cookie = new Cookie("auth_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
//            cookie.setMaxAge(24 * 60 * 60); // 1day   // 만료 시간을 제거하면 세션 쿠키가 됨
            response.addCookie(cookie);

            // 세션에 auth_token 저장
            HttpSession session = request.getSession();
            session.setAttribute("auth_token", token);
            session.setAttribute("username", username);

            return "redirect:/";
        } catch (NoSuchElementException e) {
            model.addAttribute("error", "존재하지 않는 아이디입니다.");
            return "login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(@CookieValue(value = "auth_token", required = false) String token,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        if(token != null) {
            userService.deleteToken(token);

            Cookie cookie = new Cookie("auth_token", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            // 세션 무효화
            HttpSession session = request.getSession();
            if(session != null) session.invalidate();
        }
        return "redirect:/";
    }

    // 회원 탈퇴
    @PostMapping("/bye")
    public String deleteUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        if (authentication != null) {
            userService.deleteUser(authentication.getName());

            // 명시적으로 로그아웃 처리
            try {
                request.logout();
            } catch (ServletException e) {
                e.printStackTrace(); // 로그아웃 실패 시 처리
            }

            SecurityContextHolder.clearContext(); // 로그아웃 처리

            return "redirect:/";
        }
        return "redirect:/login"; // 인증되지 않은 사용자는 로그인 페이지로 리다이렉트
    }

}
