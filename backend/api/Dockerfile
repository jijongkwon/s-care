# 빌드 스테이지
FROM gradle:8.10.2-jdk21 AS build

# 컨테이너 내부 /app 디렉토리를 기준 실행
WORKDIR /app

# 빌드 캐시를 위해 의존성 관련 파일만 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 다운로드
RUN gradle dependencies --no-daemon

# env 복사
COPY .env ./

# Firebase 서비스 계정 키 파일 복사
COPY src/main/resources/firebase/service-account-key.json ./src/main/resources/firebase/

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN gradle clean build -x test --no-daemon

# 빌드된 JAR 파일 이름 변경
RUN mv /app/build/libs/api-*-SNAPSHOT.jar /app/app.jar

# 실행 스테이지
FROM openjdk:21-jdk-slim

# 컨테이너 내부 /app 디렉토리 기준
WORKDIR /app

# Firebase 설정 디렉토리 생성
RUN mkdir -p /app/src/main/resources/firebase

# 빌드 스테이지에서 생성된 jar 파일, .env 파일, Firebase 서비스 계정 키 파일 복사
COPY --from=build /app/app.jar ./app.jar
COPY --from=build /app/.env ./.env
COPY --from=build /app/src/main/resources/firebase/service-account-key.json ./src/main/resources/firebase/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "-Duser.timezone=Asia/Seoul", "app.jar"]