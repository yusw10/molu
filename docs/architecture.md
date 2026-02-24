# Architecture

## Goal
Build a mobile-first wedding invitation service with a simple guestbook and comment feature.

## System
- Vue web app serves public invitation pages.
- Spring API handles business logic and data validation.
- Supabase Postgres stores guestbook entries.

## Why Vue + Spring
- Fits current user familiarity for faster review and editing.
- Keeps frontend and backend responsibilities explicit.
- Enables learning Supabase in a production-like flow.

## High-level Flow
1. User opens invitation page from mobile browser.
2. User submits guestbook form in Vue app.
3. Vue calls Spring API endpoint (`/api/v1/guestbook`).
4. Spring validates and writes to Supabase Postgres.
5. Vue fetches latest entries and renders list.

## Repository Structure
- `/Users/hansuk/Documents/MobileWedding/apps/web`: frontend
- `/Users/hansuk/Documents/MobileWedding/apps/api`: backend
- `/Users/hansuk/Documents/MobileWedding/supabase/sql`: schema and policies
- `/Users/hansuk/Documents/MobileWedding/docs`: documentation

## Environment Variables
- Frontend
  - `VITE_API_BASE_URL`
- Backend
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`
  - `SPRING_JPA_HIBERNATE_DDL_AUTO=validate`

## Non-goals (v0)
- Admin dashboard
- Authentication
- Realtime sockets
