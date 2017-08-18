package org.recommend.recommendbasic.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sheamus on 8/14/2017.
 */
@Component
public class RatingManager {

    // Constants
    public static final String LIKE = "LIKE";
    public static final String DISLIKE = "DISLIKE";

    private RaterRepository raterRepository;
    private SimilarityManager similarityManager;
    private RecommendationManager recommendationManager;

    @Autowired
    public void RatingManger( RaterRepository raterRepository,
                              SimilarityManager similarityManager, RecommendationManager recommendationManager){
        this.raterRepository = raterRepository;
        this.similarityManager = similarityManager;
        this.recommendationManager = recommendationManager;
    }

    /**
     * This method adds a rating to a given item by a given user. If the user
     * already has a rating for an item the current rating will be overwritten
     * by the new one.
     *
     * @param user represents the user who is rating.
     * @param item represents the item that is being rated.
     * @param rating represents the rating being given to an item by a user.
     */
    public void add(String user, String item, String rating){
        Rater r;

        if ((r = raterRepository.findByUserAndItem(user, item))
                == null){
            // If there is no existing rating, then add the new rating.

            raterRepository.save(new Rater(user, item, rating));

        }else {
            // However, if there is an existing rating then update it if need be.

            if (!r.getRating().equals(rating)){

                //Update the rating for the movie and save.
                r.setRating(rating);
                raterRepository.save(r);

            }
        }

        //Update the similarity indices and the suggestion lists
        similarityManager.update(user);
        recommendationManager.update(user);
    }

    /**
     * This method removes a rating by a user from given item
     *
     * @param user represents the user who is removing rating.
     * @param item represents the item's whose rating is to be removed.
     */
    public void remove(String user, String item){
        Rater r;
        if (( r = raterRepository.findByUserAndItem(user, item)) != null){

            // If there exists a rating by given item by given user, remove it.
            raterRepository.delete(r);

            //Update the similarity indices and the suggestion lists
            similarityManager.update(user);
            recommendationManager.update(user);
        }
    }

}
