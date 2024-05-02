docker postgres start:
    docker run -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=distcomp -p 5432:5432 -d postgres
    docker exec -it cont bash
    psql -U postgres -d distcomp
    CREATE SCHEMA IF NOT EXISTS distcomp;

docker cassandra start:
    docker run -p 9042:9042 -e CASSANDRA_KEYSPACE=distcomp -e CASSANDRA_DC=datacenter1 -e CASSANDRA_CLUSTER_NAME=myCluster -d cassandra