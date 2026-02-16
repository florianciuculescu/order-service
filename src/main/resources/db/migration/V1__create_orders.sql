create table orders (
  id uuid primary key,
  amount numeric(19, 2) not null,
  currency varchar(3) not null,
  status varchar(20) not null,
  created_at timestamp with time zone not null
);
