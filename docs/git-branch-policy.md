# Git Branch Policy (Fixed)

## Branches
- `dev`: all feature integration
- `master`: release branch only
- `feature/<name>`: work branch from `dev`
- `fix/<name>`: hotfix branch from `dev`

## Fixed Flow
1. `dev`에서 새 작업 브랜치를 생성한다.
2. 작업 후 PR의 base를 `dev`로 설정한다.
3. PR 템플릿 체크리스트를 모두 채운다.
4. 리뷰/테스트 통과 후 `dev`에 머지한다.
5. 배포 시점에만 `dev -> master` PR을 만들어 머지한다.

## Command Quick Start
```bash
git checkout dev
git pull

git checkout -b feature/<short-name>
# work...

git add .
git commit -m "feat: ..."
git push -u origin feature/<short-name>
```

## Release Merge
```bash
git checkout master
git pull
git merge --no-ff dev
git push origin master
```

## Guardrails
- `master`에 직접 커밋하지 않는다.
- `.env` 같은 비밀값은 커밋 금지.
- 코드 변경 시 `docs/changelog.md` 업데이트 필수.
