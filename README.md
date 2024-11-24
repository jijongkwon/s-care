
# 🚚 S-Care

<div align='center'>
    <img src='https://github.com/user-attachments/assets/1669732b-f465-4727-b02a-d9bb2174f361' />
</div>

## ✨ 주제

-   일상 속 스트레스 관리를 함께 해주는 서비스

## 📅 기간

-   2024.10.21 ~ 2024.11.19

## 📜 프로젝트 컨셉 및 주요 기능

### 1️⃣ 기획 배경 및 목적

-   총 130명을 대상으로 설문조사한 결과, 약 41.2%가 **거의 매일** 스트레스를 느낀다고 응답
-   또한 약 90.8%가 **스트레스 관리가 필요**히다고 응답

### 2️⃣ 주요 기능

#### 스트레스 시각화 및 알림

- 나를 투영한 펫이 스트레스 수치에 맞춰서 표정 변화

#### 산책 유도 & 지압 유도 알림

- 스트레스가 **나쁨** 구간에 해당되면 기상청 API를 호출하여 날씨 정보를 조회
- 온도, 습도, 눈/비, 풍속의 상황에 맞춰서 산책 or 지압 중 하나를 사용자에게 제안하는 알림 전송

#### 개인별 통계

- 일주일 동안 S-Care의 솔루션을 사용했던 통계 결과를 조회하여 한눈에 파악

#### 산책하기

- 실시간 위치로 산책 경로를 지도에 표시
- 산책을 3분 이상으로 할 경우, 코스 기록 저장 가능
- 산책을 5분 이상으로 할 경우, 산책 중 제일 스트레스가 낮았던 구간 기록 가능

## 💻 개발 환경 및 문서

### 1️⃣ Stacks

#### 🛠 Front-End 🛠

![Android](https://img.shields.io/badge/android-%2320232a.svg?style=for-the-badge&logo=android&logoColor=%#34A853)

#### 🛠 Back-End 🛠

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![SpringBoot](https://img.shields.io/badge/SpringBoot-%236DB33F.svg?style=for-the-badge&logo=SpringBoot&logoColor=white)
![SpringBoot](https://img.shields.io/badge/JPA-%236DB33F.svg?style=for-the-badge&logo=SpringBoot&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-005571?style=for-the-badge&logo=fastapi)

![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-black.svg?style=for-the-badge&logo=QueryDSL&logoColor=white)
![Python](https://img.shields.io/badge/python-4479A1.svg?style=for-the-badge&logo=python&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)

#### 🛠 CI / CD 🛠

![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Jenkins](https://img.shields.io/badge/jenkins-%23D24939.svg?style=for-the-badge&logo=jenkins&logoColor=white)
![NGINX](https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=nginx&logoColor=white)
![Ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white)

#### 🛠 Tools 🛠

![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitLab](https://img.shields.io/badge/gitlab-%23181717.svg?style=for-the-badge&logo=gitlab&logoColor=white)
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)
[![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)](https://imminent-hamburger-1d8.notion.site/8-0-4-0fbd317ef9d840bc9d31ea8adfa50ceb)
[![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)](https://www.figma.com/design/7WUqXjKvUcDPLKYMUa9P4Y/%EC%98%B7%EC%A7%B1?node-id=0-1&t=K68NqokoRcvm5jnd-0)

### 2️⃣ Architecture

<div align='center'>
  <img src='https://github.com/user-attachments/assets/201a1aa8-7dc9-4552-9386-2524ee520d06' >
</div>

### 3️⃣ ERD

<div align='center'>
  <img src='https://github.com/user-attachments/assets/3315086c-b253-48c6-beec-0a4c7a5216db' width='600px' >
</div>
