package org.recommend.recommendbasic.engine;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sheamus on 8/15/2017.
 */
public interface SimilarityRepository extends MongoRepository<Similarity, String> {

    List<Similarity> findByUser(String user);
    Similarity findByUserAndOther(String user, String other);

}
