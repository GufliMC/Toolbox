-- apply changes
create table "homes" (
  "id"                          uuid not null,
  "playerId"                    uuid not null,
  "name"                        varchar(255) not null,
  "worldId"                     uuid not null,
  "position"                    varchar(255) not null,
  "createdAt"                   timestamp not null,
  "updatedAt"                   timestamp not null,
  constraint "uq_homes_playerId_name" unique ("playerId","name"),
  constraint "pk_homes" primary key ("id")
);

