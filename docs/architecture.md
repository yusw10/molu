# 아키텍처

## 목표
모바일 중심 청첩장 서비스와 간단한 방명록 기능을 운영 가능한 형태로 구축합니다.

## 시스템 구성
- Vue 웹앱이 청첩장 페이지를 제공합니다.
- Spring API가 검증 및 비즈니스 로직을 처리합니다.
- Supabase Postgres가 방명록 데이터를 저장합니다.
- 관리자 페이지(`/admin`)에서 청첩장 콘텐츠를 수정해 DB에 저장합니다.

## Vue + Spring 선택 이유
- 현재 사용자 숙련도에 맞아 코드 리뷰/유지보수가 수월합니다.
- 프론트/백엔드 역할이 분리되어 확장성이 좋습니다.
- Supabase를 실서비스 형태로 학습하기 좋습니다.

## 요청 흐름
1. 사용자가 모바일 브라우저로 페이지에 접속
2. Vue에서 방명록 폼 전송
3. Spring API (`/api/v1/guestbook`) 호출
4. Spring이 Supabase Postgres에 저장/조회
5. Vue가 최신 목록을 렌더링

관리자 흐름:
1. `/admin` 접근
2. 비밀번호 검증 (`/api/v1/site-settings/admin/verify`)
3. 설정 저장 (`/api/v1/site-settings/admin`)
4. 메인 페이지에서 공개 설정 조회 (`/api/v1/site-settings`)

## 저장소 구조
- `/Users/hansuk/Documents/MobileWedding/apps/web`: 프론트엔드
- `/Users/hansuk/Documents/MobileWedding/apps/api`: 백엔드
- `/Users/hansuk/Documents/MobileWedding/supabase/sql`: 스키마/SQL
- `/Users/hansuk/Documents/MobileWedding/docs`: 문서

## 환경 변수
- 프론트엔드
  - `VITE_API_BASE_URL`
- 백엔드
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`
  - `SPRING_JPA_HIBERNATE_DDL_AUTO=validate`

## v0 범위 제외
- 관리자 대시보드
- 사용자 인증
- 실시간 소켓
