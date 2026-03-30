# K-food 수출 및 현지화 지원 플랫폼 (BigProject)

[![Frontend CI](https://github.com/FairyGina/bigProject/actions/workflows/frontend-ci.yml/badge.svg)](https://github.com/FairyGina/bigProject/actions/workflows/frontend-ci.yml)
[![Backend CI](https://github.com/FairyGina/bigProject/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/FairyGina/bigProject/actions/workflows/backend-ci.yml)

---

## 📖 프로젝트 개요

### 서비스 개발 배경

K-푸드는 2025년 수출액 **136.2억 달러**를 기록하며 역대 최대 수출 실적을 달성했습니다.  
그러나 이와 동시에 다음과 같은 심각한 문제들이 지속되고 있습니다.

| ⚠️ 문제 | 내용 |
|---------|------|
| 🔴 높은 실패율 | 직관 의존적 개발로 **80% 이상** 신제품 실패 |
| 🔴 규제 리스크 | 국가별 성분/라벨링 규제로 인한 **통관 거부** |
| 🔴 고비용 구조 | 해외 시장조사의 막대한 **시간과 비용 소모** |

> **데이터 기반의 정밀한 현지화 전략을 통해 신제품 개발의 시행착오를 줄이고,  
> K-푸드의 글로벌 경쟁력을 질적으로 고도화할 필요성에서 출발했습니다.**

---

### 서비스 목표

검증된 데이터셋과 AI를 활용한 **트렌드 기반 레시피**와 **보고서**를 생성해  
식품 기업의 **신제품 기획을 돕는 의사결정 지원 서비스**

## " K-food 수출 및 현지화 플랫폼 "

| 🍽️ AI 레시피 생성 챗봇 | 📑 보고서 생성 비교 분석 | 📊 수요 예측 시각화 |
|:---:|:---:|:---:|
| LLM 기반 맞춤형 레시피 생성 | AI 심사위원 평가 PDF 리포트 | K-Food 트렌드 데이터 시각화 |

---

## 🏗️ 시스템 아키텍처

![System Architecture](.assets/architecture.png)

이 프로젝트는 성격이 서로 다른 AI 서비스(챗봇, 분석 엔진)를 독립 컨테이너로 분리하고,  
나머지 백엔드 비즈니스 로직은 **Spring Boot 단일 애플리케이션**으로 통합 운영하는  
**준(Semi) MSA 구조**를 채택했습니다.

### 서비스 구성

| 서비스 | 기술 스택 | 역할 |
|--------|----------|------|
| **Frontend** | React 18, Vite, TailwindCSS | 사용자 인터페이스 |
| **Backend (Core)** | Spring Boot 3.x, Java 17 | 인증·DB·라우팅 통합 처리 |
| **AI Chatbot** | Python, LangGraph, GPT-4o | 레시피 생성 AI, 서비스 도우미 챗봇 (독립 컨테이너) |
| **Analysis Engine** | Python, FastAPI, Pandas | 데이터 분석·시각화 (독립 컨테이너) |
| **Infrastructure** | Azure Container Apps, PostgreSQL | 클라우드 배포 및 데이터 저장 |

> 💡 **아키텍처 선택 이유**  
> 챗봇과 분석 엔진은 Spring Boot와 언어/런타임이 완전히 달라 독립 배포가 필수입니다.  
> 반면, 나머지 비즈니스 로직은 하나의 Spring Boot 앱으로 통합함으로써  
> 팀 규모에 맞는 개발·운영 효율을 확보했습니다.

---

## 🚀 주요 기능 (Key Features)

### 1. 🥑 AI 레시피 생성 (챗봇 서비스)

![Service Flow](.assets/service_flow.png)

**레시피 생성/저장 흐름**
- **수요 예측 (LightGBM)** + **최신 외식 트렌드 (SerpAPI)** 데이터를 결합
- AI기반 레시피 생성 (LLM Chatbot) → 표준 레시피 최적화 → 사용자 요구사항 반영 → 최종 레시피 고도화
- 사용자 맞춤 레시피 DB 저장

**보고서 생성/저장 흐름**
- AI기반 보고서 생성 (타깃 국가, 페르소나, 금액설정, 레시피 정보)
- **데이터 통합 분석**: 마케팅(인플루언서) 타깃 분석 / 수출 통관 리스크 진단 / 식품 안전성 규제 검토 / 국가별 레시피 평가 점수화 및 피드백
- 비즈니스 리포트 생성 및 PDF 저장

### 2. 📊 데이터 분석 서비스

**분석 데이터 소스**

| 데이터 | 내용 |
|--------|------|
| **수출 데이터** | 국가/품목 수출입 트렌드 분석, 수출액/증량 추이 추출, 물품 트렌드·경제 상관분석 |
| **아마존 리뷰 데이터** | NLP 기반 감성 분석, 소비자 경험 지표 산출 (품질·경험·만족요인) |

**비즈니스 보고서 산출물**
- AI 심사위원 분석 지표 제공 (맛·건강·가격·충점)
- SWOT 및 KPI 기반 시장성 검토
- 최적 레시피 제안 및 전략 수립

### 3. 📑 자동 보고서 생성 (PDF Automation)

- **원클릭 리포트**: AI 심사위원단이 레시피를 평가하고 PDF 문서로 자동 생성
- 타깃 국가, 타깃 페르소나, 마케팅 전략까지 포함한 종합 비즈니스 리포트

### 4. 🔐 보안 구성

- **Naver OAuth2 로그인**: 기존 네이버 아이디로 빠르고 안전한 로그인
- **CSRF & CORS 정책**: 브라우저 요청 CSRF 토큰 검증, 내부 서비스 간 통신 예외 처리
- **보안 쿠키**: `SameSite=None`, `Secure` 속성으로 크로스 도메인 인증 유지
- **데이터 암호화**: BCrypt 알고리즘으로 민감 정보 저장

---

## 🔄 CI/CD 파이프라인

개발자가 코드를 푸시하면 GitHub Actions가 자동으로 빌드·테스트·배포를 수행합니다.

```
[Code Push]
    │
    ├─ frontend/** 변경 → Frontend CI (Node.js Build → Lint → Test → Docker Image)
    ├─ src/**     변경 → Backend CI  (Gradle Build → Unit Test)
    ├─ analysis-engine/** 변경 → Analysis CI (Docker Image Build)
    └─ ai-chatbot/**     변경 → Chatbot CI  (Docker Image Build)
                                    │
                               [ACR Push]
                          Azure Container Registry
                          (bpback / bpfront / bpanalysis / bpchatbot)
                                    │
                            [ACA Auto Deploy]
                         Azure Container Apps 자동 업데이트
```

### CI/CD 단계 요약

| 단계 | 내용 |
|------|------|
| **1. Trigger** | `main` 또는 `cloud` 브랜치 push 시 변경 경로 감지 후 실행 |
| **2. Build & Test** | Frontend(Node.js 빌드), Backend(Gradle 컴파일·테스트), Python 서비스(Docker 빌드) |
| **3. Container Registry** | Azure Container Registry(ACR)에 버전 태그 이미지 저장 |
| **4. Deploy** | Azure Container Apps(ACA)가 새 이미지를 감지하여 자동 배포 |

---

## 🛠️ 기술 스택 (Tech Stack)

### Frontend
- **Framework**: React 18, Vite 5
- **Style**: TailwindCSS
- **State**: Context API, React Query

### Backend (Core)
- **Framework**: Spring Boot 3.2, Spring Security 6
- **Language**: Java 17
- **Build**: Gradle

### AI & 분석 서비스 (독립 컨테이너)
- **Language**: Python 3.11
- **Libraries**: Pandas, Scikit-learn, LightGBM, LangChain, LangGraph
- **AI Model**: OpenAI GPT-4o
- **Framework**: FastAPI, Gradio
- **외부 API**: SerpAPI (트렌드 검색)

### DevOps & Infrastructure
- **Container**: Docker, Docker Compose
- **CI/CD**: GitHub Actions
- **Cloud**: Azure Container Apps (Serverless)
- **Registry**: Azure Container Registry (ACR)
- **Database**: PostgreSQL 16 (Managed Service)

---

## 🏃‍♂️ 시작하기 (Getting Started)

### 로컬 개발 환경 설정

**1. 저장소 클론**
```bash
git clone https://github.com/FairyGina/bigProject.git
cd bigProject
```

**2. 환경 변수 설정**  
`.env.example` 파일을 복사하여 `.env` 파일을 생성하고, 필요한 API 키를 입력합니다.
```bash
cp .env.example .env
# .env 파일에서 OpenAI API Key, Naver Client ID 등을 설정하세요
```

**3. 서비스 실행 (Docker Compose)**
```bash
docker-compose up -d --build
```

**4. 접속 주소**

| 서비스 | 주소 |
|--------|------|
| **Frontend** | http://localhost:5173 |
| **Backend API** | http://localhost:8080 |
| **AI Chatbot** | http://localhost:7860 |
| **Analysis Engine** | http://localhost:8000 |

---

## 🔧 트러블 슈팅 & 아키텍처 의사결정

프로젝트를 진행하며 겪었던 기술적 고민과 문제 해결 과정을 정리했습니다.

---

### 1. Azure VM → Azure Container Apps 전환

![아키텍처 선정 이유 1](.assets/page_1.png)

초기에는 Azure VM에서 Docker Compose로 서비스를 운영했으나, Blue-Green 배포 시 비용 2배 증가 및 VM 프로비저닝 지연 문제가 발생했습니다. 이를 해결하기 위해 **Azure Container Apps(ACA)** 로 전환하여 Serverless 기반 자동 스케일링, Rolling Update를 통한 무중단 배포, Scale to 0을 통한 비용 절감을 달성했습니다.

---

### 2. 모놀리식 →  MSA 구조 전환

![아키텍처 선정 이유 2](.assets/page_2.png)

Python 기반 AI 서비스(챗봇·분석 엔진)가 추가되면서 단일 서버 구조의 한계가 드러났습니다. 각 서비스를 독립 컨테이너로 분리하여 **개별 스케일링 정책**을 적용하고, AI 모듈의 Idle 시간을 최적화하여 불필요한 비용 발생을 차단했습니다.

---

### 3. Cloud 환경 인증 문제 (SSL Termination & 프로토콜 유실)

![트러블 슈팅 - Cloud 인증](.assets/page_3.png)

ACA 배포 후 OAuth2 Redirect URL 불일치 및 인증 실패가 발생했습니다. ACA Ingress의 SSL Termination으로 인해 백엔드에서 HTTPS를 인식하지 못하는 것이 원인이었으며, Nginx에서 **X-Forwarded-Proto 헤더를 명시적으로 주입**하여 Spring Security가 프로토콜을 올바르게 판단하도록 해결했습니다.

---

### 4. JWT와 CSRF 보안 설정 (Cross-Origin 환경)

![트러블 슈팅 - JWT & CSRF](.assets/page_4.png)

로컬에서는 정상 동작하나 ACA 배포 후 POST/PUT API 호출 시 403 에러가 발생했습니다. Cross-Origin 환경에서 CSRF 쿠키가 유실되는 문제를 확인하고, 필터에서 **csrfToken을 명시적으로 호출**하여 응답 헤더에 포함시키고 쿠키 속성을 `SameSite=None`, `Secure=True`로 설정하여 해결했습니다.

---

### 5. 데이터 병목 및 API 응답 경량화

![트러블 슈팅 - 데이터 경량화](.assets/page_5.png)

레시피 목록 조회 시 응답이 6-7초로 지연되는 문제가 발생했습니다. 목록 조회용 **경량 DTO를 별도 구성**하여 필수 정보만 반환하고, 100KB 이상 이미지는 자동으로 리사이징하여 썸네일로 변환함으로써 **응답 속도를 50% 단축**(2-3초)했습니다.

---

### 6. CI/CD 파이프라인 최적화

![CI/CD 최적화](.assets/page_6.png)

`paths` 필터 기반 **선택적 빌드**로 변경된 서비스만 CI를 실행하여 불필요한 빌드를 방지하고, **다중 레이어 캐싱**(pip, npm, Docker Layer)으로 빌드 시간을 단축했습니다. 또한 배포 전 Shift-Left 검증(flake8, npm lint, gradlew build)과 ACA 리비전 자동 교체를 통한 **무중단 배포**를 구현했습니다.

---

## 팀 소개

| 이름 | 역할 | 주요 담당 |
|------|------|-----------|
| **김지나 (팀장)** | AI / Frontend / Backend / Data | 프로젝트 진행 총괄 및 팀·일정 관리, 프로젝트 회의록·주간 보고서 관리, 전체 시스템 아키텍처 설계, 시스템 통합 및 아키텍처 총괄 관리, AI 레시피 챗봇 에이전트 파이프라인 구축·개발, 국가별 부적합 정보 데이터셋 전처리·생성, 국가별 부적합 정보 에이전트 설계·개발 |
| **김경훈** | Cloud / Data | 데이터 분석 및 웹 시각화 설계·개발, 수요 & 트렌드 예측 데이터 파이프라인 구축·개발,  클라우드 CI/CD 배포 파이프라인 구축, Azure Container Apps 클라우드 인프라 배포 |
| **안지현** | AI / Frontend / Backend / Data | 프로젝트 회의록 작성 및 관리, 전체 시스템 아키텍처 설계, AI 레시피 챗봇 에이전트 개발 및 웹 연결, 알레르기 정보 데이터셋 전처리·생성, 알레르기 정보 에이전트 설계·개발 |
| **이승윤** | AI / Data / ML | 수요 & 트렌드 예측 모델 설계·개발, 성능 실험·비교 및 평가 지표 산출, 국가별 TOP-N 결과 리포트 생성 파이프라인 구축, 도움말 챗봇 에이전트 설계·개발 및 웹 연결, AI 레시피 챗봇 프롬프트 엔지니어링 |
| **박권호** | AI / Frontend / Backend | 프론트엔드 PM, 시스템 통합 및 아키텍처 검토, 레시피 재료 추출 기능 개발, 인플루언서 추천 및 생성 에이전트 설계·개발, 리포트 에이전트 설계·개발, 공지사항 게시판 설계·개발 |
| **차수진** | AI / Frontend / Backend / DB | 레시피 평가 AI 페르소나 모델 설계 및 로직 개발, 최종 레시피 보고서 AI, 비교/분석 에이전트 설계·개발, 전체 시스템 DB 스키마 설계 및 데이터 모델링, 기업별 멀티테넌시 회원 관리 및 SNS 연동로그인, 웹 보안 시스템 설계·구축, 비대면 회의 실시간 채팅 개발 |
