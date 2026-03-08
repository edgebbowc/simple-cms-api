package com.malgn.service;

import com.malgn.domain.RefreshToken;
import com.malgn.domain.User;
import com.malgn.dto.request.LoginRequest;
import com.malgn.exception.ForbiddenException;
import com.malgn.exception.NotFoundException;
import com.malgn.repository.RefreshTokenRepository;
import com.malgn.repository.UserRepository;
import com.malgn.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 로그인 → Access + Refresh Token 모두 발급
    public Map<String, String> login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        User user = userRepository.findByUsername(request.username())
                .orElseThrow();

        String accessToken = jwtTokenProvider
                .generateAccessToken(user.getUsername(), user.getRole().name());

        String refreshToken = jwtTokenProvider
                .generateRefreshToken(user.getUsername());

        // Refresh Token DB 저장 (이미 있으면 갱신)
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtTokenProvider.getRefreshExpiration() / 1000);

        refreshTokenRepository.findByUsername(user.getUsername())
                .ifPresentOrElse(
                        rt -> rt.update(refreshToken, expiresAt),  // 기존 토큰 갱신
                        () -> refreshTokenRepository.save(         // 새로 저장
                                RefreshToken.builder()
                                        .username(user.getUsername())
                                        .token(refreshToken)
                                        .expiresAt(expiresAt)
                                        .build())
                );

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "role", user.getRole().name()
        );
    }

    // Access Token 재발급
    @Transactional
    public Map<String, String> reissue(String refreshToken) {

        // ① Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ForbiddenException("유효하지 않은 Refresh Token입니다.");
        }

        // ② DB에서 Refresh Token 조회
        RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ForbiddenException(
                        "존재하지 않는 Refresh Token입니다."));

        // ③ 만료 여부 확인
        if (savedToken.isExpired()) {
            refreshTokenRepository.delete(savedToken);
            throw new ForbiddenException("만료된 Refresh Token입니다. 다시 로그인해주세요.");
        }

        // ④ 새 Access Token 발급
        User user = userRepository.findByUsername(savedToken.getUsername())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtTokenProvider
                .generateAccessToken(user.getUsername(), user.getRole().name());

        // ⑤ Refresh Token Rotation (보안 강화: 재발급 시 Refresh Token도 교체)
        String newRefreshToken = jwtTokenProvider
                .generateRefreshToken(user.getUsername());
        LocalDateTime newExpiresAt = LocalDateTime.now()
                .plusSeconds(jwtTokenProvider.getRefreshExpiration() / 1000);
        savedToken.update(newRefreshToken, newExpiresAt);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        );
    }

    // 로그아웃 → DB에서 Refresh Token 삭제
    @Transactional
    public void logout(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }
}
