-- apply changes
create table `players` (
  `id`                          varchar(40) not null,
  `name`                        varchar(255) not null,
  `seentAt`                     datetime(6),
  `createdAt`                   datetime(6) not null,
  `updatedAt`                   datetime(6) not null,
  constraint "uq_players_name" unique (`name`),
  constraint "pk_players" primary key (`id`)
);

create table `warps` (
  `id`                          varchar(40) not null,
  `name`                        varchar(255) not null,
  `worldId`                     varchar(40) not null,
  `position`                    varchar(255) not null,
  `createdAt`                   datetime(6) not null,
  `updatedAt`                   datetime(6) not null,
  constraint "uq_warps_name" unique (`name`),
  constraint "pk_warps" primary key (`id`)
);

