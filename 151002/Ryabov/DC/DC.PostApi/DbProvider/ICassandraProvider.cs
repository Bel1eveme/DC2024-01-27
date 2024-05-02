namespace Forum.PostApi.DbProvider;

public interface ICassandraProvider
{
     IClusterSession GetSession();
}