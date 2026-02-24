# Runbook

## 1. Tooling
- Node.js 22 LTS
- Java 21
- Git

## 2. Clone and setup
```bash
git clone <repo-url>
cd MobileWedding
```

## 3. Environment
Copy env samples:
```bash
cp apps/web/.env.example apps/web/.env
cp apps/api/.env.example apps/api/.env
```

Set Supabase DB connection in `apps/api/.env`.

## 4. Run web
```bash
cd apps/web
npm install
npm run dev
```

## 5. Run api
```bash
cd apps/api
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
set -a && source .env && set +a
./gradlew bootRun
```

## 6. Build for an old/unused computer
```bash
cd apps/web
npm ci
npm run build

cd ../api
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew clean build
```

## 7. Smoke test checklist
- Web loads on mobile width.
- `POST /api/v1/guestbook` works.
- Guestbook list renders latest entry.
- Korean text is displayed correctly.

## 8. Release
- Merge into `dev`.
- Verify with checklist.
- Merge `dev` into `main`.
- Tag release (`v0.1.0`).
