package com.example.blogproject.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    // id 체크
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam("username") String username) {
        User user = userService.findUser(username);
        boolean exists = user != null;
        return ResponseEntity.ok(exists);
    }

    // email 체크
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email) {
        User user = userService.findEmail(email);
        boolean exists = user != null;
        return ResponseEntity.ok(exists);
    }
}
