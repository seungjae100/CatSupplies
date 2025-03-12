package com.web.catsupplies.user.application;

import com.web.catsupplies.user.domain.Role;
import com.web.catsupplies.user.domain.User;
import com.web.catsupplies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public RegisterResponse register(RegisterRequest request) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // Builder 를 통한 데이터 요청입력
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(Role.USER)
                .build();

        // 요청 DB에 저장
        userRepository.save(user);

        // 요청 완료에 따른 응답 반환
        return RegisterResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));
        // 비밀번호 검증 ( 클라이언트에서 요청이 온 비밀번호와 유저의 DB에서 가져온 비밀번호를 비교했는데 다르다면)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return LoginResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

}
