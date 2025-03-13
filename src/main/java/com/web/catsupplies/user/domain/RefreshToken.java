package com.web.catsupplies.user.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 1209600) // 14일
public class RefreshToken {

    @Id // Spring Data Redis 에서 Key 역할
    private String email;
    private String refreshToken;
}
