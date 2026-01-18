-- apply changes
create table "players" (
  "id"                          uuid not null,
  "name"                        varchar(255) not null,
  "seentAt"                     timestamp,
  "createdAt"                   timestamp not null,
  "updatedAt"                   timestamp not null,
  constraint "uq_players_name" unique ("name"),
  constraint "pk_players" primary key ("id")
);

create table "warps" (
  "id"                          uuid not null,
  "name"                        varchar(255) not null,
  "worldId"                     uuid not null,
  "position"                    varchar(255) not null,
  "createdAt"                   timestamp not null,
  "updatedAt"                   timestamp not null,
  constraint "uq_warps_name" unique ("name"),
  constraint "pk_warps" primary key ("id")
);

