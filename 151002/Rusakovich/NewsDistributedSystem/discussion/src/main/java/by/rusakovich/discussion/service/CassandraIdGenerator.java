package by.rusakovich.discussion.service;

public class CassandraIdGenerator {
    public static Long getId(){
        return System.currentTimeMillis();
    }
}
