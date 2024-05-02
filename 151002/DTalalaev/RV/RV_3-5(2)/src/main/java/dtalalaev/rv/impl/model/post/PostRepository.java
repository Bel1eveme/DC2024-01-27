package dtalalaev.rv.impl.model.post;

import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface PostRepository extends CrudRepository<Post, BigInteger> {
}
