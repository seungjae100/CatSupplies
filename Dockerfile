# 1. Java 17
FROM openjdk:17-jdk-slim

# 2. JAR 파일 경로 설정 (Maven은 target 디렉토리에 jar 생성됨)
ARG JAR_FILE=target/*.jar

# 3. wait-for-it.sh 복사
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# 4. JAR 파일 복사
COPY ${JAR_FILE} app.jar

# 5. wait-for-it.sh를 통해 DB가 떠 있는지 확인하고 Spring Boot 실행
ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app.jar"]
