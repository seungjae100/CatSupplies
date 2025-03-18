package com.web.catsupplies.user.repository;

import com.web.catsupplies.user.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    String email(String email);
}
