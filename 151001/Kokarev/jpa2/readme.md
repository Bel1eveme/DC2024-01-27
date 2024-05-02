docker postgres start:
docker run -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=distcomp -p 5432:5432 postgres
docker exec -it cont bash
psql -U postgres -d distcomp
CREATE SCHEMA IF NOT EXISTS distcomp;