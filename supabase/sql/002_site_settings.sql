create table if not exists public.site_settings (
  id int primary key,
  admin_password_hash text,
  payload text not null default '{}',
  updated_at timestamptz not null default now()
);

insert into public.site_settings (id, payload, updated_at)
values (1, '{}', now())
on conflict (id) do nothing;
