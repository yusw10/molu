# Supabase First Setup (Beginner Guide)

This guide is written for first-time Supabase users.

## 0) What you will prepare
- Email login for Supabase
- Project name (example: `mobile-wedding`)
- Strong DB password

## 1) Create Supabase project
1. Open Supabase dashboard and sign in.
2. Click `New project`.
3. Choose organization.
4. Set:
   - Name: `mobile-wedding`
   - Database Password: create and save in password manager
   - Region: closest to Korea (or your primary users)
5. Click `Create new project` and wait until ready.

## 2) Create table (guestbook)
1. Open SQL Editor.
2. Create `New query`.
3. Paste SQL from:
   - `/Users/hansuk/Documents/MobileWedding/supabase/sql/001_guestbook_entries.sql`
4. Click `Run`.
5. Confirm table exists in `Table Editor` as `guestbook_entries`.

## 3) Get DB connection info for Spring
1. In Supabase dashboard, go to `Project Settings` -> `Database`.
2. Find `Connection string`.
3. Prefer the `Pooler` host for JDBC connection (recommended).
4. Keep values:
   - Pooler host (`aws-0-<region>.pooler.supabase.com`)
   - Port (`5432`)
   - Database name (`postgres`)
   - User (`postgres.<project-ref>`)
   - Password (the one you created)

## 4) Configure backend env file
1. Create env file from sample:
   - copy `/Users/hansuk/Documents/MobileWedding/apps/api/.env.example`
   - to `/Users/hansuk/Documents/MobileWedding/apps/api/.env`
2. Fill exact values:
   - `SPRING_DATASOURCE_URL=jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME=postgres.<project-ref>`
   - `SPRING_DATASOURCE_PASSWORD=<db-password>`

## 5) Run backend with env loaded
In terminal:
```bash
cd /Users/hansuk/Documents/MobileWedding/apps/api
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
set -a
source .env
set +a
./gradlew bootRun
```

Success condition:
- No DB authentication error
- Server starts on `http://localhost:8080`

## 6) Run frontend
In another terminal:
```bash
cd /Users/hansuk/Documents/MobileWedding/apps/web
cp .env.example .env
npm install
npm run dev
```

Open shown local URL and test form submit.

## 7) Troubleshooting
- If DB SSL error: ensure URL includes `?sslmode=require`.
- If auth failed: verify DB password in `/Users/hansuk/Documents/MobileWedding/apps/api/.env`.
- If `No route to host` on `db.<project-ref>.supabase.co`, switch to Pooler host and user `postgres.<project-ref>`.
- If CORS issue: ensure frontend uses `VITE_API_BASE_URL=http://localhost:8080`.
- If table missing: rerun SQL migration in Supabase SQL Editor.
