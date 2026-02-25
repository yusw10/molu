# MobileWedding

문서 우선(Documentation-first) 방식으로 진행하는 모바일 청첩장 프로젝트입니다.

## 기술 스택
- 프론트엔드: Vue 3 + TypeScript + Vite
- 백엔드: Spring Boot 3 (Java 21) + JPA
- 데이터: Supabase Postgres (방명록/축하 메시지)

## 저장소 구조
- `docs/`: 기획/개발/운영 문서
- `apps/web/`: Vue 프론트엔드
- `apps/api/`: Spring 백엔드
- `supabase/sql/`: SQL 스키마 및 정책

## 브랜치 전략
- `master`: 릴리스 브랜치
- `dev`: 개발 통합 브랜치
- `feature/*`: 기능 작업 브랜치

## 문서 기반 작업 흐름
1. 코딩 전 `docs/specs/`에 명세 작성/수정
2. 코드 구현
3. 관련 문서 및 `docs/changelog.md` 업데이트
4. `docs/quality/checklist.md` 기준 검수

## 빠른 시작
1. `/Users/hansuk/Documents/MobileWedding/docs/README.md` 확인
2. `/Users/hansuk/Documents/MobileWedding/docs/local-run-for-non-dev.md` 순서대로 실행
