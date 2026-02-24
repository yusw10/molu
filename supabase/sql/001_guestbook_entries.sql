create extension if not exists pgcrypto;

create table if not exists public.guestbook_entries (
  id uuid primary key default gen_random_uuid(),
  name varchar(40) not null,
  message varchar(400) not null,
  attending boolean not null default false,
  created_at timestamptz not null default now()
);

create index if not exists idx_guestbook_entries_created_at on public.guestbook_entries (created_at desc);
