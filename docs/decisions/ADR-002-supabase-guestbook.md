# ADR-002: Supabase for Guestbook

## Status
Accepted

## Context
Need a simple, low-maintenance database for guestbook comments.

## Decision
Store guestbook data in Supabase Postgres.
Frontend calls Spring API, Spring writes/reads DB.

## Consequences
- Pros
  - Managed Postgres with dashboard and backups.
  - Easy future extension (storage, auth, edge functions).
- Cons
  - Must manage DB credentials carefully.
  - Need SQL migration discipline.
