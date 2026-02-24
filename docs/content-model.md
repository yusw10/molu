# 콘텐츠 모델

## 청첩장 콘텐츠 (예정)
- 신랑/신부 이름
- 예식 날짜/시간
- 장소 + 지도 링크
- 인삿말
- 갤러리 이미지 목록
- 계좌 정보

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
- `attending`: 선택 (기본값 false)

### API 형태
#### POST `/api/v1/guestbook`
```json
{
  "name": "하객 이름",
  "message": "축하합니다!",
  "attending": true
}
```

#### GET `/api/v1/guestbook`
```json
[
  {
    "id": "uuid",
    "name": "하객 이름",
    "message": "축하합니다!",
    "attending": true,
    "createdAt": "2026-02-23T10:00:00Z"
  }
]
```
