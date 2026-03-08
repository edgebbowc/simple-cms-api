package com.malgn.config;

import com.malgn.security.JwtAuthenticationFilter;
import com.malgn.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // H2 콘솔 허용
                        .requestMatchers("/h2-console/**").permitAll()
                        // 로그인
                        .requestMatchers("/api/auth/login").permitAll()
                        // 리프레쉬 토큰 재발급
                        .requestMatchers(HttpMethod.POST, "/api/auth/reissue").permitAll()
                        // 콘텐츠 조회는 인증 불필요
                        .requestMatchers(HttpMethod.GET, "/api/contents/**").permitAll()
                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )
                /**
                 * 필터단에서 예외 처리
                 */
                .exceptionHandling(ex -> ex //
                        // 비로그인 → 401
                        .authenticationEntryPoint((request, response, e) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(
                                    objectMapper.writeValueAsString(
                                            Map.of("status", 401, "message", "로그인이 필요합니다.")
                                    )
                            );
                        })
                )
                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())) // H2 콘솔용
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
