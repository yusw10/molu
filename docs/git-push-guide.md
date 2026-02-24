# GitHub Connect and Push Guide

## Current status
- Local git repo exists.
- Remote is not configured yet.

## 1) Create GitHub repository
1. In GitHub, create a new empty repo.
2. Do not add README/gitignore/license in GitHub UI (already exists locally).

## 2) Add remote locally
```bash
cd /Users/hansuk/Documents/MobileWedding
git remote add origin <your-github-repo-url>
git remote -v
```

## 3) First commit
```bash
git add .
git commit -m "chore: initialize docs-first vue-spring-supabase starter"
```

## 4) Push to GitHub
If default branch is `main`:
```bash
git branch -M main
git push -u origin main
```

## 5) Ongoing branch workflow
```bash
git checkout -b dev
git push -u origin dev

git checkout -b feature/guestbook-ui
# work...
git add .
git commit -m "feat: add guestbook ui and api wiring"
git push -u origin feature/guestbook-ui
```

## Notes
- If GitHub asks authentication, use GitHub CLI login or PAT.
- Keep secrets only in `.env`, never commit real credentials.
