# 비개발자용 로컬 실행 가이드

이 문서는 "코드는 잘 모르지만, git으로 받아서 내 컴퓨터에서 실행"하고 싶은 사람 기준입니다.

## 0. 목표
- 웹 화면을 내 컴퓨터에서 띄운다.
- 방명록 작성/조회가 동작하는지 확인한다.

## 1. 한 번만 설치
macOS 기준:
1. Homebrew 설치(없으면)
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

개발 브랜치 기준으로 확인하려면:
```bash
git checkout dev
git pull
```

## 3. Supabase 준비
1. Supabase 프로젝트 생성
2. SQL Editor에서 아래 파일 실행
- `/Users/hansuk/Documents/MobileWedding/supabase/sql/001_guestbook_entries.sql`
3. Database 설정 화면에서 Pooler host 확인

## 4. API 환경설정 파일 만들기
```bash
cp apps/api/.env.example apps/api/.env
```

`apps/api/.env`를 열어서 아래 3개 값 입력:
- `SPRING_DATASOURCE_URL` : Pooler host로 된 JDBC URL
- `SPRING_DATASOURCE_USERNAME` : `postgres.<project-ref>`
- `SPRING_DATASOURCE_PASSWORD` : 본인 DB 비밀번호

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
- 터미널에 local URL 표시 (예: `http://localhost:5173`)

## 7. 동작 테스트
1. 웹 페이지 접속
2. 이름/메시지 입력 후 등록
3. 방명록 목록에 바로 보이면 성공

## 8. 자주 나는 오류
- `Unable to locate a Java Runtime`
  - Java 설치 또는 `JAVA_HOME` 설정 문제
- `No route to host`
  - Supabase direct host 사용 중일 가능성 큼
  - Pooler host로 변경
- `password authentication failed`
  - `.env` 비밀번호 재확인
- `npm: command not found`
  - `brew install node` 필요

## 9. 업데이트 받는 방법
```bash
git checkout dev
git pull
```
