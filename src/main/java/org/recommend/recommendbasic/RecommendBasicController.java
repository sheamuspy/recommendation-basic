package org.recommend.recommendbasic;

import org.recommend.recommendbasic.engine.RatingManager;
import org.recommend.recommendbasic.engine.Recommendation;
import org.recommend.recommendbasic.engine.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by sheamus on 8/16/2017.
 */

@RestController
@RequestMapping("/")
public class RecommendBasicController {

    private RatingManager ratingManager;
    private RecommendationRepository recommendationRepository;


    @Autowired
    public RecommendBasicController(
            RatingManager ratingManager, RecommendationRepository recommendationRepository){
        this.ratingManager = ratingManager;
        this.recommendationRepository = recommendationRepository;
    }


    @RequestMapping(value = "/like/{user}/{item}", method = RequestMethod.GET)
    public void likeItem(
            @PathVariable("user") String user, @PathVariable("item") String item){

                ratingManager.add(user,item, RatingManager.LIKE);

    }


    @RequestMapping(value = "/dislike/{user}/{item}", method = RequestMethod.GET)
    public void dislikeItem(
            @PathVariable("user") String user, @PathVariable("item") String item){

                ratingManager.add(user,item, RatingManager.DISLIKE);

    }

    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public List<Recommendation> usersRecommendations(@PathVariable("user") String user){

                return recommendationRepository.findByUserOrderByWeightDesc(user);
    }



}
