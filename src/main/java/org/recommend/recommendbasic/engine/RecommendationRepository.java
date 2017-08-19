package org.recommend.recommendbasic.engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sheamus on 8/15/2017.
 */
public interface RecommendationRepository extends MongoRepository<Recommendation, String> {

    List<Recommendation> findByUserOrderByWeightDesc(String user);

    Page<Recommendation> findByUserOrderByWeightDesc(String user, Pageable pageable);

    void deleteByUser(String user);

}
