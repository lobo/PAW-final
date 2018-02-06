-- ALTER TABLE users
--  ADD score DOUBLE PRECISION;
--
-- ALTER TABLE users
--  ADD clanId INT,
--  ADD FOREIGN KEY (clanId) REFERENCES clans(clanId) ON DELETE SET NULL;
--
--  ALTER TABLE


ALTER table clans add score DOUBLE PRECISION default 0;
alter table clans add column wins integer not null default 0;
alter table clans add column battles integer not null default 0;
alter table clans add column image varchar(100) not null default 'null';
CREATE TABLE clansbattle(clanid INT PRIMARY KEY,versusid INT,initialscore DOUBLE PRECISION NOT NULL, FOREIGN KEY(clanid) REFERENCES clans ON DELETE CASCADE,FOREIGN KEY(versusid) REFERENCES clans ON DELETE SET NULL);
