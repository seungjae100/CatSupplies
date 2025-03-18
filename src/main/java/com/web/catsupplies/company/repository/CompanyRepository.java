package com.web.catsupplies.company.repository;

import com.web.catsupplies.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByEmail(String email); // 중복되는 이메일 확인

    Optional<Company> findByEmail(String email);
}
