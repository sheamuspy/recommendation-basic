package org.recommend.recommendbasic.engine;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by sheamus on 8/15/2017.
 */
public interface SimilarsRepository extends MongoRepository<Similars, String> {

    public Similars findByUser(String user);

}
