# 비개발자용 로컬 실행 가이드

코드를 잘 몰라도, Git으로 받아 내 컴퓨터에서 실행해보는 기준 문서입니다.

## 0. 목표
- 청첩장 화면을 로컬에서 띄운다.
- 방명록 작성/조회가 되는지 확인한다.

## 1. 1회 설치
macOS 기준:
1. Homebrew 설치 (없다면)
2. Node 설치
```bash
brew install node
```
3. Java 21 설치
```bash
brew install openjdk@21
```

## 2. 프로젝트 받기
```bash
git clone https://github.com/yusw10/molu.git
cd molu
```

개발 브랜치 확인:
```bash
git checkout dev
git pull
```

## 3. Supabase 준비
1. Supabase 프로젝트 생성
2. SQL Editor에서 아래 파일 실행
- `/Users/hansuk/Documents/MobileWedding/supabase/sql/001_guestbook_entries.sql`
3. `Database` 설정에서 Pooler host 확인

## 4. API 환경변수 파일 만들기
```bash
cp apps/api/.env.example apps/api/.env
```

`apps/api/.env`에서 아래 3개 값 수정:
- `SPRING_DATASOURCE_URL` : Pooler JDBC URL
- `SPRING_DATASOURCE_USERNAME` : `postgres.<project-ref>`
- `SPRING_DATASOURCE_PASSWORD` : DB 비밀번호

## 5. API 서버 실행 (터미널 1)
```bash
cd apps/api
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
set -a && source .env && set +a
./gradlew bootRun
```

정상 기준:
- 로그에 `Tomcat started on port 8080`

## 6. 웹 서버 실행 (터미널 2)
```bash
cd apps/web
cp -n .env.example .env
npm install
npm run dev
```

정상 기준:
- 터미널에 로컬 URL 표시 (예: `http://localhost:5173`)

## 7. 동작 확인
1. 웹 페이지 접속
2. 이름/메시지 입력 후 등록
3. 방명록 목록에 반영되는지 확인

## 8. 자주 나는 오류
- `Unable to locate a Java Runtime`
  - Java 설치 또는 `JAVA_HOME` 미설정
- `No route to host`
  - Supabase direct host가 막힌 상태일 수 있음
  - Pooler host로 변경
- `password authentication failed`
  - `.env` 비밀번호 재확인
- `npm: command not found`
  - `brew install node` 실행

## 9. 최신 코드 받기
```bash
git checkout dev
git pull
```
