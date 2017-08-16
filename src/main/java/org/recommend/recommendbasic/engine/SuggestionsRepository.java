package org.recommend.recommendbasic.engine;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sheamus on 8/15/2017.
 */
public interface SuggestionsRepository extends MongoRepository<Suggestions, String> {

    public Suggestions findByUser(String user);

    public void deleteByUser(String user);

}
