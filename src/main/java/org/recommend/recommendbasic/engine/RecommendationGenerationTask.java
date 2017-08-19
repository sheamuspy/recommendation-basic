package org.recommend.recommendbasic.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sheamus on 8/18/2017.
 */
@Component
@EnableScheduling
public class RecommendationGenerationTask {

    private static final Logger log = LoggerFactory.getLogger(RecommendationGenerationTask.class);

    @Autowired
    RaterRepository raterRepository;
    @Autowired
    SimilarityManager similarityManager;
    @Autowired
    RecommendationManager recommendationManager;


    @Scheduled(fixedDelay = 20000)
    public void generateRecommendations(){

        log.info("Generating new recommendations");

        List<Rater> rAllraters = raterRepository.findAll();

        Set<String> allraters = rAllraters.stream().map(Rater::getUser).collect(Collectors.toSet());

        for(String rater : allraters){

            similarityManager.update(rater);

            recommendationManager.update(rater);
        }

    }


}
