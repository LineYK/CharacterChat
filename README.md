# CharacterChat (캐릭터 챗)

Spring Boot 기반의 실시간 AI 캐릭터 채팅 서비스 백엔드 프로젝트입니다.
사용자는 다양한 성격의 AI 캐릭터와 실시간으로 대화할 수 있으며, 가상 재화(크레딧)를 사용하여 고성능 AI 모델을 선택하거나 구독 서비스를 이용할 수 있습니다.

## 🚀 주요 기능 (Features)

* **실시간 채팅 (WebSocket/STOMP)**
  * STOMP 프로토콜을 활용한 실시간 양방향 통신
  * `[img:tag]` 형식의 시스템 프롬프트 태그를 통한 캐릭터 감정/이미지 동적 렌더링
* **유연한 AI 모델 연동 (Strategy Pattern)**
  * Groq(OpenAI 호환), Google Gemini 등 다양한 LLM API를 Strategy/Factory 패턴으로 관리
  * 모델별 가격(크레딧) 차등 적용 및 동적 모델 스위칭 기능
* **안전한 지갑(Wallet) 및 크레딧 시스템**
  * 비관적 락(Pessimistic Lock)과 PENDING(예약) 패턴을 활용한 동시성 제어 및 무결성 보장
  * 자동 결제 실패 시 보상 트랜잭션 없이 안전하게 롤백되는 구조 구현
* **결제 및 구독 시스템 (Toss Payments 연동)**
  * 단건 크레딧 패키지 결제 및 Webhook 멱등성 처리
  * 자동 결제 및 매일 크레딧을 지급받는 출석체크 형태의 구독(Subscription) 모델 지원

## 🎮 확장 계획 (Future Roadmap)

* **Unity 연동 3D 아바타 채팅**
  * 유니티(Unity) 클라이언트와 연동하여 3D 아바타가 AI의 감정 및 상태에 맞춰 동적으로 애니메이션을 수행하도록 확장
* **채팅 기반 데이팅(Dating) 시스템**
  * 사용자와 AI 캐릭터 간의 상호작용(채팅 횟수, 대화 내용 기반 친밀도 등)을 추적
  * 특정 친밀도 기점에 도달하면 일반 채팅에서 **비주얼 노벨 형태의 데이팅 모드**로 전환되는 게임화(Gamification) 요소 도입

## 🏗 아키텍처 및 기술 스택

* **Language**: Java 21
* **Framework**: Spring Boot 3.5.x
* **Database**: PostgreSQL (Spring Data JPA)
* **Security**: Spring Security + JWT
* **Communication**: WebSocket (STOMP), Spring WebClient (비동기 HTTP API 호출)
* **Design Pattern**: Facade Pattern (애플리케이션 계층 분리), Strategy/Factory Pattern (AI 모델 및 결제망 연동)

## 📈 진행 사항 (Progress)

프로젝트는 MVP 기능을 넘어 비즈니스 로직 고도화 단계에 있습니다.

* [x] **MVP 핵심 기능**: 회원가입/로그인, 실시간 STOMP 채팅, DB 영속성, Groq/Gemini 연동 완료
* [x] **백엔드 고도화**: 비관적 락 기반 지갑(Wallet) 시스템, 트랜잭션 이벤트(`@TransactionalEventListener`)를 활용한 비동기 AI 호출, Facade 계층 도입
* [x] **결제 시스템 (Phase 1)**: 단건 크레딧 결제, Toss 결제 승인/웹훅/환불 처리 완료
* [x] **구독 시스템 (Phase 2)**: 구독 플랜(Subscription Plan) 설계, 매일 출석체크(Claim) 크레딧 지급 서비스 및 컨트롤러 구현 완료
* [ ] **구독 자동 갱신**: 매월 정기 결제 갱신 스케줄러 및 Billing Key 연동 (진행 중)
* [ ] **크레딧 고도화**: 무료/유료 크레딧 출처 분리 및 FIFO 차감, 이벤트 크레딧 만료 처리 (예정)

## ⚙️ 실행 방법 (Getting Started)

프로젝트 루트에 `.env` 파일을 생성하고 아래 환경 변수를 설정하세요.

```properties
DB_URL=jdbc:postgresql://localhost:5432/characterchat
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret_key_at_least_32_characters
ADMIN_PASSWORD=your_admin_password
OPENAI_API_KEY=your_groq_or_openai_api_key
SPRING_AI_GOOGLE_GENAI_API_KEY=your_gemini_api_key
toss.payment.secret-key=your_toss_secret_key
toss.payment.base-url=https://api.tosspayments.com/v1
```
