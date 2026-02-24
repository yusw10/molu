# Content Model

## Invitation Content (planned)
- couple names
- wedding date/time
- venue + map link
- greeting text
- gallery image list
- account information

## Guestbook Entry
### Fields
- `id` (uuid)
- `name` (varchar, 40)
- `message` (varchar, 400)
- `attending` (boolean)
- `created_at` (timestamptz)

### Validation Rules
- `name`: required, 1..40 chars
- `message`: required, 1..400 chars
- `attending`: optional (default false)

### API shape
#### POST `/api/v1/guestbook`
```json
{
  "name": "Guest Name",
  "message": "Congratulations!",
  "attending": true
}
```

#### GET `/api/v1/guestbook`
```json
[
  {
    "id": "uuid",
    "name": "Guest Name",
    "message": "Congratulations!",
    "attending": true,
    "createdAt": "2026-02-23T10:00:00Z"
  }
]
```
