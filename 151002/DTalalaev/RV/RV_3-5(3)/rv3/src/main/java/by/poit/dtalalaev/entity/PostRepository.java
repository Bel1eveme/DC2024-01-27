package by.poit.dtalalaev.entity;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;


import java.math.BigInteger;

@Repository
public interface PostRepository extends CassandraRepository<Post, BigInteger> {
}
