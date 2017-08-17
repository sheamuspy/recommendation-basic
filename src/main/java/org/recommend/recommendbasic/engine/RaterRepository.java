package org.recommend.recommendbasic.engine;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sheamus on 8/14/2017.
 */
public interface RaterRepository extends MongoRepository<Rater, String> {

    public Rater findByUserAndItemAndRating(String user, String item, String rating);
    public Rater findByUserAndItem(String user, String item);
    public List<Rater> findByUserAndRating(String user, String rating);
    public List<Rater> findByItemAndRating(String item, String rating);
    public List<Rater> findByItem(String item);

    public List<String> findItemByUser(String user);
}
