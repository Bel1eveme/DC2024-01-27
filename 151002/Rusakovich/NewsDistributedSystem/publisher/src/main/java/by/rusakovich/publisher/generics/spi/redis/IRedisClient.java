package by.rusakovich.publisher.generics.spi.redis;

public interface IRedisClient<ResponseTO, HashKey>{
    void put(HashKey key, ResponseTO responseTo);
    ResponseTO get(HashKey key);
    void delete(HashKey key);
}
