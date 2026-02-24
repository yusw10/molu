# Git 브랜치 정책 (고정)

## 브랜치 역할
- `dev`: 기능 통합 브랜치
- `master`: 릴리스 브랜치
- `feature/<name>`: `dev`에서 분기하는 기능 브랜치
- `fix/<name>`: `dev`에서 분기하는 수정 브랜치

## 고정 작업 흐름
1. `dev`에서 작업 브랜치를 생성합니다.
2. 작업 후 PR의 base를 `dev`로 설정합니다.
3. PR 템플릿 체크리스트를 모두 채웁니다.
4. 리뷰/테스트 통과 후 `dev`에 머지합니다.
5. 배포 시점에만 `dev -> master` PR을 만들어 머지합니다.
   - 릴리스 PR 템플릿: `/Users/hansuk/Documents/MobileWedding/.github/PULL_REQUEST_TEMPLATE/release-to-master.md`

## 빠른 명령어
```bash
git checkout dev
git pull

git checkout -b feature/<short-name>
# 작업

git add .
git commit -m "feat: ..."
git push -u origin feature/<short-name>
```

## 릴리스 머지
```bash
git checkout master
git pull
git merge --no-ff dev
git push origin master
```

## 운영 가드레일
- `master` 직접 커밋 금지
- `.env` 등 비밀값 커밋 금지
- 코드 변경 시 `docs/changelog.md` 업데이트 필수
