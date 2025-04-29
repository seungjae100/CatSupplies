# 1. Java 17
FROM openjdk:17-jdk-slim

# 2. jar 파일 경로 설정 (Maven은 target 디렉토리에 jar 생성됨)
ARG JAR_FILE=target/*.jar

# 3. JAR 파일을 컨테이너 내부로 복사
COPY ${JAR_FILE} app.jar

# 4. JAR 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]