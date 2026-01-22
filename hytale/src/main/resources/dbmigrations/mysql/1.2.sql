-- apply changes
create table `bans` (
  `id`                          varchar(40) not null,
  `playerId`                    varchar(40) not null,
  `issuerId`                    varchar(40),
  `reason`                      varchar(255) not null,
  `expiresAt`                   datetime(6),
  `canceledAt`                  datetime(6),
  `createdAt`                   datetime(6) not null,
  constraint "pk_bans" primary key (`id`)
);

create table `mutes` (
  `id`                          varchar(40) not null,
  `playerId`                    varchar(40) not null,
  `issuerId`                    varchar(40),
  `reason`                      varchar(255) not null,
  `expiresAt`                   datetime(6),
  `canceledAt`                  datetime(6),
  `createdAt`                   datetime(6) not null,
  constraint "pk_mutes" primary key (`id`)
);

-- foreign keys and indices
create index "ix_bans_playerId" on `bans` (`playerId`);
alter table `bans` add constraint "fk_bans_playerId" foreign key (`playerId`) references `players` (`id`) on delete restrict on update restrict;

create index "ix_bans_issuerId" on `bans` (`issuerId`);
alter table `bans` add constraint "fk_bans_issuerId" foreign key (`issuerId`) references `players` (`id`) on delete restrict on update restrict;

create index "ix_mutes_playerId" on `mutes` (`playerId`);
alter table `mutes` add constraint "fk_mutes_playerId" foreign key (`playerId`) references `players` (`id`) on delete restrict on update restrict;

create index "ix_mutes_issuerId" on `mutes` (`issuerId`);
alter table `mutes` add constraint "fk_mutes_issuerId" foreign key (`issuerId`) references `players` (`id`) on delete restrict on update restrict;

