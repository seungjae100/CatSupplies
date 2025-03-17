package com.web.catsupplies.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 생성시간, 수정시간 자동 관리 기능 활성화
@Getter
@MappedSuperclass // 엔티티가 아니며, 단지 부모 클래스로서 필드를 상속시켜줍니다.
@EntityListeners(AuditingEntityListener.class) // 생성, 수정 시점을 자동으로 관리해주는 역할
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티가 처음으로 저장될 때 자동으로 생성 시간 설정
    @Column(updatable = false) // 한 번 생성되면 변경되지 않도록
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 자동으로 수정 시간 설정
    private LocalDateTime updatedAt;

}
