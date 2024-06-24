package com.example.blogproject.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // main
    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/loginform")
    public String loginForm() { return "login"; }

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
            userService.findUser(user.getUsername());
            isUsernameDuplicated = true;
            bindingResult.rejectValue("username", "error.user", "이미 존재하는 id입니다.");
        } catch (NoSuchElementException e) {
            // 사용자 이름이 없는 경우 처리
        }

        try {
            userService.findEmail(user.getEmail());
            isEmailDuplicated = true;
            bindingResult.rejectValue("email", "error.user", "이미 존재하는 이메일입니다.");
        } catch (NoSuchElementException e) {
            // 이메일이 없는 경우 처리
        }

        if (isUsernameDuplicated || isEmailDuplicated) {
            return "register";
        }

        userService.join(user);
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
            User user = userService.findUser(username);
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
}
