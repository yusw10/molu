# ADR-001: 기술 스택 선택

## 상태
채택됨

## 배경
프로젝트 오너가 React/Next보다 Vue와 Spring에 익숙합니다.

## 결정
Vue 3 + Spring Boot + Supabase Postgres를 사용합니다.

## 영향
- 장점
  - 리뷰/수정 속도가 빠름
  - 런칭 후 유지보수 가능성 높음
  - Supabase 학습 효과 확보
- 단점
  - 프론트 단독 스택보다 초기 설정이 다소 많음
  - Java 런타임 필요
