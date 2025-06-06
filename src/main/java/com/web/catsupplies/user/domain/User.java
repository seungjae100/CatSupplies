package com.web.catsupplies.user.domain;

import com.web.catsupplies.common.constant.RegexPatterns;
import com.web.catsupplies.order.domain.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity // User엔티티로 사용자 정보를 테이블과 매핑한다.
@Getter // 모든 필드를 Getter를 자동 생성, Setter를 하지 않은 이유는 불변성을 유지하기 위해서
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자 자동 생성
@Builder
@Table(name = "users") // 엔티티와 매핑될 테이블 이름을 USERS 로 지정, 예약어인 user를 피하기 위해서
public class User extends BaseTimeEntity {

    // 기본 키 , 자동 증가 전략을 사용, MySQL AUTO_INCREMENT 와 연결
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 고유키 , 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 사용자 이름
    @Column(nullable = false)
    private String name;

    // 사용자 비밀번호
    @Column(nullable = false)
    private String password;

    // 사용자 핸드폰
    @Column(nullable = false)
    private String phone;

    // 사용자 주소
    @Column(nullable = false)
    private String address;

    // 사용자 권한 (USER) - ENUM 값 저장 방식은 문자열 (STRING)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    // Order와 연관관계
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();


    // 회원 생성
    public static User create(String email, String name, String password,String phone, String address) {
        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .phone(phone)
                .address(address)
                .role(Role.USER)
                .build();
    }

    // 회원 수정에 필요한 메서드
    public void changeName(String name) {
        this.name = name;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    // 회원 삭제
    @Column(nullable = false)
    private boolean deleted = false;

    public void remove() {
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }




}
