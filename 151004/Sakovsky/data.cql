 CREATE KEYSPACE IF NOT EXISTS distcomp
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE distcomp;

CREATE TABLE IF NOT EXISTS tbl_comment (
  "country" TEXT,
  "tweetId" INT,
  "id" INT,
  "content" TEXT,
  PRIMARY KEY (("country"), "tweetId", "id")
);