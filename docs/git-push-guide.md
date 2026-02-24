# GitHub 연결 및 푸시 가이드

## 현재 상태
- 로컬 저장소는 준비됨
- 원격 저장소 연결 후 푸시 가능

## 1) GitHub 저장소 생성
1. GitHub에서 빈 저장소 생성
2. README/gitignore/license는 GitHub UI에서 생성하지 않음 (로컬에 이미 존재)

## 2) 로컬에 원격 추가
```bash
cd /Users/hansuk/Documents/MobileWedding
git remote add origin <github-repo-url>
git remote -v
```

## 3) 첫 커밋
```bash
git add .
git commit -m "chore: initialize docs-first vue-spring-supabase starter"
```

## 4) 첫 푸시
기본 브랜치가 `master`라면:
```bash
git push -u origin main:master
```

## 5) 일상 작업 브랜치 흐름
```bash
git checkout dev
git pull

git checkout -b feature/guestbook-ui
# 작업...
git add .
git commit -m "feat: add guestbook ui and api wiring"
git push -u origin feature/guestbook-ui
```

## 참고
- 인증 요청 시 GitHub 토큰(PAT) 또는 GitHub 로그인 필요
- 비밀값은 `.env`에만 저장하고 커밋하지 않음
