CREATE TABLE IF NOT EXISTS clans (
  clanid INTEGER IDENTITY PRIMARY KEY,
  NAME VARCHAR (100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
  userid BIGINT IDENTITY PRIMARY KEY,
  username varchar(100) UNIQUE,
  password varchar(100),
  profileImage varchar(100),
  score DOUBLE,
  clanID INTEGER,
  FOREIGN KEY (clanId) REFERENCES clans(clanId)  ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS factories (
  userid BIGINT,
  type INTEGER,
  amount DOUBLE,
  inputReduction DOUBLE,
  outputMultiplier DOUBLE,
  costReduction DOUBLE,
  level INTEGER,
  PRIMARY KEY(userid, type),
    FOREIGN KEY (userid) REFERENCES users(userid)  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wealths (
  userid BIGINT,
  resourceType INTEGER,
  production DOUBLE,
  storage DOUBLE,
  lastUpdated BIGINT,
  PRIMARY KEY(userid, resourceType),
  FOREIGN KEY (userid) REFERENCES users(userid)  ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS stockMarket (
  resourceType INT PRIMARY KEY,
  amount DOUBLE PRECISION
);
