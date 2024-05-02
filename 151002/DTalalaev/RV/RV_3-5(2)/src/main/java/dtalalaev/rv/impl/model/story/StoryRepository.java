package dtalalaev.rv.impl.model.story;

import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface StoryRepository extends CrudRepository<Story, BigInteger> {
}
