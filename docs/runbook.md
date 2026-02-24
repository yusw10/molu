# 운영 실행서 (Runbook)

## 1. 준비 도구
- Node.js 22+ (npm 포함)
- Java 21
- Git

## 2. 저장소 준비
```bash
git clone <repo-url>
cd MobileWedding
```

## 3. 환경 변수 설정
샘플 파일 복사:
```bash
cp apps/web/.env.example apps/web/.env
cp apps/api/.env.example apps/api/.env
```

`apps/api/.env`에 Supabase 연결 정보를 입력합니다.

Supabase SQL Editor에서 아래 파일을 순서대로 실행합니다.
- `/Users/hansuk/Documents/MobileWedding/supabase/sql/001_guestbook_entries.sql`
- `/Users/hansuk/Documents/MobileWedding/supabase/sql/002_site_settings.sql`

## 4. 프론트 실행
```bash
cd apps/web
npm install
npm run dev
```

## 5. 백엔드 실행
```bash
cd apps/api
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
set -a && source .env && set +a
./gradlew bootRun
```

## 6. 빌드 (다른 PC 재현용)
```bash
cd apps/web
npm ci
npm run build

cd ../api
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew clean build
```

## 7. 스모크 테스트
- 모바일 폭에서 화면이 깨지지 않는지 확인
- `POST /api/v1/guestbook` 동작 확인
- 방명록 조회 목록 렌더링 확인
- 한글 텍스트 표시 확인

## 8. 릴리스
- `dev`로 머지
- 체크리스트 검수
- `dev -> master` 머지
- 태그 발행 (`v0.1.0`)
