package com.web.catsupplies.user.repository;

import com.web.catsupplies.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email); // 이메일 중복 확인

    Optional<User> findByEmail(String email); // User 객체에 이메일이 존재하는지 확인
}
