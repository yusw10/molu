# ADR-002: 방명록 저장소로 Supabase 사용

## 상태
채택됨

## 배경
방명록 댓글을 단순하고 안정적으로 운영할 DB가 필요합니다.

## 결정
방명록 데이터는 Supabase Postgres에 저장합니다.
프론트는 Spring API를 호출하고, Spring이 DB에 읽기/쓰기를 수행합니다.

## 영향
- 장점
  - 관리형 Postgres와 대시보드 제공
  - 향후 Storage/Auth/Edge Functions로 확장 가능
- 단점
  - DB 인증 정보 관리 필요
  - SQL 마이그레이션 관리 필요
