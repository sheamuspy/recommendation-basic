package org.recommend.recommendbasic;

import org.recommend.recommendbasic.engine.RatingManager;
import org.recommend.recommendbasic.engine.Suggestions;
import org.recommend.recommendbasic.engine.SuggestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by sheamus on 8/16/2017.
 */

@Controller
@RequestMapping("/")
public class RecommendBasicController {

    private RatingManager ratingManager;
    private SuggestionsRepository suggestionsRepository;


    @Autowired
    public RecommendBasicController(
            RatingManager ratingManager, SuggestionsRepository suggestionsRepository){
        this.ratingManager = ratingManager;
        this.suggestionsRepository = suggestionsRepository;
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
    public Suggestions usersRecommendations(@PathVariable("user") String user){

                return suggestionsRepository.findByUser(user);
    }



}
