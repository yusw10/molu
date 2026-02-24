# MobileWedding

Documentation-first wedding invitation project.

## Stack
- Frontend: Vue 3 + TypeScript + Vite
- Backend: Spring Boot 3 (Java 21) + JPA
- Data: Supabase Postgres (guestbook/comments)

## Repository Layout
- `docs/` documentation and decisions
- `apps/web/` Vue frontend
- `apps/api/` Spring backend
- `supabase/sql/` SQL migrations and policies

## Branch Strategy
- `main`: release-ready code
- `dev`: integration branch
- `feature/*`: task branches

## Documentation Flow
1. Create/update spec in `docs/specs/` before coding.
2. Implement code.
3. Update related docs and `docs/changelog.md`.
4. Validate with checklist in `docs/quality/checklist.md`.

## Quick Start
1. Read `/Users/hansuk/Documents/MobileWedding/docs/README.md`
2. Follow `/Users/hansuk/Documents/MobileWedding/docs/runbook.md`
