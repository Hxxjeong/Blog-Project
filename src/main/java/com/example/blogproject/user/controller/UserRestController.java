package com.example.blogproject.user.controller;

import com.example.blogproject.user.service.UserService;
import com.example.blogproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {
    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenizer jwtTokenizer;
//    private final RefreshTokenService refreshTokenService;

    // id 체크
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam("username") String username) {
        User user = userService.findByUsername(username);
        boolean exists = user != null;
        return ResponseEntity.ok(exists);
    }

    // email 체크
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);
        boolean exists = user != null;
        return ResponseEntity.ok(exists);
    }

    // jwt
//    // 로그인
//    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody @Valid LoginReqDto reqDto,
//                                BindingResult bindingResult,
//                                HttpServletResponse response) {
//        log.debug("로그인 시도: {}", reqDto.getUsername());
//
//        // username, password가 정해진 형식에 맞지 않을 때
//        if(bindingResult.hasErrors()) return new ResponseEntity(HttpStatus.BAD_REQUEST);
//
//        // 가입한 유저인지 확인
//        User user = userService.findByUsername(reqDto.getUsername());
//        if(user == null)
//            return new ResponseEntity("존재하지 않는 아이디입니다.", HttpStatus.UNAUTHORIZED);
//
//        // 비밀번호가 일치하는지 확인
//        if(!passwordEncoder.matches(reqDto.getPassword(), user.getPassword()))
//            return new ResponseEntity("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
//
//        // 유저의 role 정보 확인
//        List<String> roles = user.getRoles().stream()
//                .map(Role::getName)
//                .collect(Collectors.toList());
//
//        // 토큰 발급
//        String accessToken = jwtTokenizer.createAccessToken(
//                user.getId(),
//                user.getEmail(),
//                user.getName(),
//                user.getUsername(),
//                roles
//        );
//        String refreshToken = jwtTokenizer.createRefreshToken(
//                user.getId(),
//                user.getEmail(),
//                user.getName(),
//                user.getUsername(),
//                roles
//        );
//
//        // DB에 토큰 저장
//        RefreshToken refreshTokenEntity = RefreshToken.builder()
//                .value(refreshToken)
//                .userId(user.getId())
//                .build();
//
//        refreshTokenService.create(refreshTokenEntity);
//
//        // 응답으로 보낼 객체
//        UserLoginRspDto rspDto = UserLoginRspDto.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .userId(user.getId())
//                .name(user.getName())
//                .build();
//
//        // 토큰을 쿠키로 전달
//        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
////        accessTokenCookie.setHttpOnly(true);    // 쿠키값을 JS에서 접근할 수 없게 함
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT / 1000));  // cookie: s | jwt: ms
//
//        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
////        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUNT / 1000));
//
//        response.addCookie(accessTokenCookie);
//        response.addCookie(refreshTokenCookie);
//
//        log.debug("로그인 성공: {}", reqDto.getUsername());
//
//        return new ResponseEntity(rspDto, HttpStatus.OK);
//    }
//
//    // refresh token 생성
//    @PostMapping("/refreshToken")
//    public ResponseEntity createRefreshToken(HttpServletRequest request,
//                                             HttpServletResponse response) {
//        String refreshToken = null;
//        Cookie[] cookies = request.getCookies();
//
//        // 쿠키로부터 refresh token 얻어오기
//        if(cookies != null) {
//            for(Cookie cookie: cookies) {
//                if("refreshToken".equals(cookie.getName())) {
//                    refreshToken = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        // 쿠키가 없는 경우 예외 처리
//        if(refreshToken == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//        // 토큰으로부터 정보 얻기
//        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
//        Long userId = Long.valueOf((Integer) claims.get("userId"));
//
//        User user = userService.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));
//
//        // access token 생성
//        List roles = (List) claims.get("roles");
//
//        String accessToken = jwtTokenizer.createAccessToken(
//                userId,
//                user.getEmail(),
//                user.getName(),
//                user.getUsername(),
//                roles
//        );
//
//        // 쿠키에 토큰 저장
//        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT / 1000));
//
//        response.addCookie(accessTokenCookie);
//
//        // 응답 객체
//        UserLoginRspDto rspDto = UserLoginRspDto.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .userId(userId)
//                .name(user.getName())
//                .build();
//
//        return new ResponseEntity(rspDto, HttpStatus.OK);
//    }
}
