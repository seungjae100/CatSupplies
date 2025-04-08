package com.web.catsupplies.company.repository;

import com.web.catsupplies.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    // 기업 이메일 중복 확인 탈퇴된 회원은 제외하고
    boolean existsByEmailAndDeletedFalse(String email); // 중복되는 이메일 확인

    // 로그인, 기업 정보 조회 탈퇴된 회원은 제외하고
    Optional<Company> findByEmailAndDeleltedFalse(String email);

    // 기업 ID 로 조회 탈퇴한 회원은 제외하고
    Optional<Company> finByIdAndDeletedFalse(Long id);
}
