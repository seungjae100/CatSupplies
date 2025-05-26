### 😺 CatSupplies - 이커머스 백엔드 프로젝트
---

### 🛠 기술스택

#### 🔧 Backend
---
- Java 17, Spring Boot 3.3.2
- Spring Data JPA, QueryDSL
- Spring Security, JWT (AccessToken/RefreshToken)
- Redis (RefreshToken 저장)
- Validation, Lombok

#### 🗃 Database
---
- MySQL 8
- Docker 기반 컨테이너 실행


#### 📘 API 문서화
---
- Swagger UI (springdoc-openapi-starter-webmvc-ui:2.6.0)


#### 🔐 인증 / 인가
---
- JWT 기반 인증 구현
- AccessToken: HttpOnly Cookie 저장
- RefreshToken: Redis 저장
- 사용자(User) / 기업(Company) 인증 분리 구현


### 🚀 배포 및 실행
---
- AWS EC2 (t3.small 인스턴스)
- Docker + Docker Compose (docker-compose.prod.yml)
- Elastic IP로 고정 URL 운영


### 주요 기능
---
1. 🛍️ 제품 관리 (기업 전용)

- 제품 등록 / 수정 / 삭제
- Stock 엔티티 연결, StockHistory 자동 저장

2. 📦 재고 및 이력 관리

- 출고/복원 트랜잭션 처리
- 재고 음수 방지 및 품절 처리 로직 포함

3. 🧾 주문 / 결제 (사용자 전용)

- 주문 생성 및 취소
- 결제 상태 변경/저장/조회까지 흐름 구현
- 재고 검증 후 주문 가능 여부 판단

4. 🔐 인증 및 예외 처리

- 사용자, 기업 회원가입 및 로그인 분리
- JWT 필터 적용으로 인증/인가 처리
- Swagger에서 Authorize 버튼으로 테스트 가능
- 예외 발생 시 일관된 메시지 (ApiErrorResponse) 반환

 
### 🧪 테스트
---
- Swagger UI로 전체 API 테스트 가능
- 예외 케이스에 대한 테스트 코드 일부 작성

