package com.example.blogproject.global.auth.config;

import com.example.blogproject.global.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // csrf 토큰 해제
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .requestMatchers("/", "/register", "/userregform").permitAll()
                        .requestMatchers("/@**").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/loginform")
                        .loginProcessingUrl("/login")   // 로그인을 진행하는 url
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                )
                // 동시 로그인 제어
                .sessionManagement(sessionManagement -> sessionManagement
                        .maximumSessions(1) // default: -1 (무제한)
                        .maxSessionsPreventsLogin(true) // default: false - 먼저 로그인 된 브라우저가 expired
                )
                .userDetailsService(customUserDetailsService);

        return http.build();
    }
}

/* jwt
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // csrf 토큰 해제
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .requestMatchers("/", "/register", "/userregform").permitAll()
                        .requestMatchers("/", "/login", "/refreshToken", "loginform", "/logout").permitAll()
                        .requestMatchers("/@**").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                // filter 생성
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer),
                        UsernamePasswordAuthenticationFilter.class)
//                // formLogin 사용 X
//                .formLogin(form -> form.disable())
                .formLogin(form -> form
                        .loginPage("/loginform")
                        .loginProcessingUrl("/login")   // 로그인을 진행하는 url
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 로그인 사용 X
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(cors -> cors
                        .configurationSource(configurationSource())
                )
                // 예외 처리
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        return http.build();
    }

    // CORS 설정
    public CorsConfigurationSource configurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET", "POST", "DELETE"));

        source.registerCorsConfiguration("/**", config);    // 모든 요청에 설정 적용

        return source;
    }
 */