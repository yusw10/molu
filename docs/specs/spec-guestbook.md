# Spec: Guestbook v1

## Purpose
Allow guests to leave short congratulation messages.

## User Story
As a guest, I can write my name and message and submit it from mobile quickly.

## Inputs
- Name
- Message
- Optional attending toggle

## Outputs
- Success toast
- New entry appears in the list sorted by newest

## Edge Cases
- Empty values should be rejected.
- Too long values should be rejected.
- API failure should show retry message.

## Definition of Done
- Form works on mobile.
- Validation on frontend and backend.
- SQL schema applied.
- Docs updated (`content-model`, `changelog`, `runbook`).
