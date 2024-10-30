# --- 빌드 스테이지 ---
FROM python:3.12.2-slim AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 도구 및 필수 시스템 라이브러리 설치
RUN apt-get update && \
    apt-get install -y gcc build-essential && \
    rm -rf /var/lib/apt/lists/*

# requirements.txt 복사
COPY requirements.txt .

# dependencies 설치 (빌드 단계)
RUN pip install --no-cache-dir -r requirements.txt --target /app/dependencies

# --- 실행 스테이지 ---
FROM python:3.12.2-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 설치한 패키지 복사
COPY --from=builder /app/dependencies /usr/local/lib/python3.12/site-packages

# 애플리케이션 코드 복사
COPY . .

# FastAPI 애플리케이션 실행 (포트 8000)
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
