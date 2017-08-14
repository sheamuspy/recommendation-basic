package org.recommend.recommendbasic.engine;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by sheamus on 8/14/2017.
 */
public interface UserRepository extends MongoRepository<User, String> {


}
