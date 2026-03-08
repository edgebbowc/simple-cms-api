package com.malgn;

import com.malgn.domain.Role;
import com.malgn.domain.User;
import com.malgn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("local")  // local 프로필에서만 실행
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 이미 데이터가 있으면 스킵
        if (userRepository.count() > 0) return;

        userRepository.saveAll(List.of(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123")) // 비밀번호 인코딩해서 저장
                        .role(Role.ADMIN)
                        .build(),
                User.builder()
                        .username("user1")
                        .password(passwordEncoder.encode("user1234"))
                        .role(Role.USER)
                        .build(),
                User.builder()
                        .username("user2")
                        .password(passwordEncoder.encode("user1234"))
                        .role(Role.USER)
                        .build()
        ));
    }
}
