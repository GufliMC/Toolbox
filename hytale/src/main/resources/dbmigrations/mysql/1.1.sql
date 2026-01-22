-- apply changes
create table `homes` (
  `id`                          varchar(40) not null,
  `playerId`                    varchar(40) not null,
  `name`                        varchar(255) not null,
  `worldId`                     varchar(40) not null,
  `position`                    varchar(255) not null,
  `createdAt`                   datetime(6) not null,
  `updatedAt`                   datetime(6) not null,
  constraint "uq_homes_playerId_name" unique (`playerId`,`name`),
  constraint "pk_homes" primary key (`id`)
);

