package com.web.catsupplies.user.repository;

import com.web.catsupplies.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 중복 확인 (탈퇴한 유저 제외)
    boolean existsByEmailAndDeletedFalse(String email);

    // 로그인, 사용자 정보 조회 시 (탈퇴한 유저 제외)
    Optional<User> findByEmailAndDeletedFalse(String email);

    // 사용자 ID로 조회 (탈퇴한 유저 제외)
    Optional<User> findByIdAndDeletedFalse(Long id);
}
