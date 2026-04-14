 🛒 React + Spring Boot 쇼핑몰 프로젝트

 📌 프로젝트 소개

React와 Spring Boot를 활용한 CRUD 기반 웹 애플리케이션입니다.
사용자 인증부터 상품 관리까지 전체적인 흐름을 구현했습니다.

---

 🧩 주요 기능

 🔐 인증

* JWT 기반 로그인 / 로그아웃
* 인증 필터 구현

 📦 상품 관리

* 상품 등록 / 조회 / 수정 / 삭제
* REST API 설계

 📊 상태 관리

* Zustand를 활용한 전역 상태 관리

---

 🛠 기술 스택

| 구분       | 기술                          |
| -------- | --------------------------- |
| Frontend | React, Zustand, React Query |
| Backend  | Spring Boot, JPA            |
| DB       | MySQL                       |
| 기타       | Git, REST API               |

---

 ⚙️ 실행 방법

 Frontend

```bash
npm install
npm start
```

 Backend

```bash
./gradlew bootRun
```


---

 ✍️ 느낀점

* 프론트와 백엔드를 함께 개발하며 전체 흐름을 이해할 수 있었습니다.
* 상태관리(Zustand)를 활용해 복잡한 상태를 효율적으로 관리할 수 있었습니다.
* 인증 및 보안 처리의 중요성을 체감했습니다.

---

 📸 화면

<img width="1898" height="904" alt="스크린샷 2026-04-14 165733" src="https://github.com/user-attachments/assets/0a67e54e-ca55-43d6-8ad1-6cf53fe388ff" /><img width="1912" height="907" alt="스크린샷 2026-04-14 165944" src="https://github.com/user-attachments/assets/702315c8-1fc6-4644-a239-554edc7e63a4" />
<img width="1916" height="906" alt="스크린샷 2026-04-14 165924" src="https://github.com/user-attachments/assets/0de801f6-d7f4-4b2f-9772-25caa3d39775" />
<img width="1917" height="905" alt="스크린샷 2026-04-14 165808" src="https://github.com/user-attachments/assets/16b291b8-3f06-4df4-b139-aae06a7b1bb5" />



---

 📌 개선 예정

* 결제 기능 추가
* UI/UX 개선
* 테스트 코드 작성
