# 콘텐츠 모델

## 청첩장 설정 데이터 (`site_settings.payload`)
### 기본 정보
- `greeting` (인삿말)
- `wedding.date` (YYYY-MM-DD)
- `wedding.time` (HH:mm)
- `wedding.venueName`
- `wedding.address`
- `wedding.addressDetail`

### 인물 정보
- `couple.groom`
- `couple.bride`
- `couple.groomFather`
- `couple.groomMother`
- `couple.brideFather`
- `couple.brideMother`

### 연락/계좌/공유
- `contact.groomPhone`
- `contact.bridePhone`
- `accounts.groom`
- `accounts.bride`
- `share.invitationUrl`
- `share.kakaoMessage`

### 미디어
- `media.heroImage` (메인 첫 화면)
- `media.coupleImage` (부부 소개)
- `media.venueImage` (예식장)
- `media.gallery1`
- `media.gallery2`
- `media.gallery3`
- `media.videoUrl`

### 스토리 섹션
- `story.firstMeet`
- `story.proposal`
- `story.weddingDay`

## 방명록 엔트리
### 필드
- `id` (uuid)
- `name` (varchar, 40)
- `message` (varchar, 400)
- `attending` (boolean)
- `created_at` (timestamptz)

### 검증 규칙
- `name`: 필수, 1..40자
- `message`: 필수, 1..400자
- `attending`: 선택 (기본 false)

## API
### 공개 설정 조회
- `GET /api/v1/site-settings`

### 관리자 인증/설정
- `POST /api/v1/site-settings/admin/setup`
- `POST /api/v1/site-settings/admin/verify`
- `PUT /api/v1/site-settings/admin`

### 방명록
- `GET /api/v1/guestbook`
- `POST /api/v1/guestbook`
