# Supabase 최초 설정 가이드 (초보자용)

Supabase를 처음 사용하는 기준으로 작성했습니다.

## 0) 준비물
- Supabase 로그인 계정
- 프로젝트 이름 (예: `mobile-wedding`)
- 강한 DB 비밀번호

## 1) Supabase 프로젝트 생성
1. Supabase 대시보드 로그인
2. `New project` 클릭
3. 조직 선택
4. 아래 값 입력
   - Name: `mobile-wedding`
   - Database Password: 저장해둘 비밀번호
   - Region: 주요 사용자와 가까운 리전
5. `Create new project` 클릭 후 생성 완료까지 대기

## 2) 테이블 생성 (guestbook)
1. `SQL Editor` 이동
2. `New query` 생성
3. 아래 파일 SQL 붙여넣기
   - `/Users/hansuk/Documents/MobileWedding/supabase/sql/001_guestbook_entries.sql`
4. `Run` 실행
5. `Table Editor`에서 `guestbook_entries` 생성 확인

## 3) Spring용 DB 연결 정보 확인
1. `Project Settings -> Database` 이동
2. `Connection string` 확인
3. JDBC는 `Pooler` 호스트를 우선 사용
4. 아래 값을 기록
   - Pooler host (`aws-0-<region>.pooler.supabase.com` 형태)
   - Port (`5432`)
   - Database (`postgres`)
   - User (`postgres.<project-ref>`)
   - Password (직접 설정한 값)

## 4) 백엔드 env 파일 설정
1. 샘플 파일 복사
   - `/Users/hansuk/Documents/MobileWedding/apps/api/.env.example`
   - `/Users/hansuk/Documents/MobileWedding/apps/api/.env`
2. 값 입력
   - `SPRING_DATASOURCE_URL=jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME=postgres.<project-ref>`
   - `SPRING_DATASOURCE_PASSWORD=<db-password>`

## 5) 백엔드 실행
```bash
cd /Users/hansuk/Documents/MobileWedding/apps/api
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
set -a
source .env
set +a
./gradlew bootRun
```

성공 기준:
- DB 인증 오류 없음
- 서버가 `http://localhost:8080`에서 실행됨

## 6) 프론트 실행
```bash
cd /Users/hansuk/Documents/MobileWedding/apps/web
cp .env.example .env
npm install
npm run dev
```

표시되는 로컬 URL로 접속해 폼 전송 테스트

## 7) 자주 발생하는 오류
- DB SSL 오류: URL에 `?sslmode=require` 포함 여부 확인
- 인증 실패: `apps/api/.env` 비밀번호 재확인
- `db.<project-ref>.supabase.co`에서 `No route to host`: Pooler 호스트로 변경
- CORS 문제: `VITE_API_BASE_URL=http://localhost:8080` 확인
- 테이블 없음: SQL 마이그레이션 재실행
