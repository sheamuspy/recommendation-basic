package org.recommend.recommendbasic.engine;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sheamus on 8/14/2017.
 */
public class RatingManager {

    @Autowired
    private SimilarityManager similarityManager;
    @Autowired
    private SuggestionsManager suggestionsManager;

    public void RatingManger(){}

    public void add(RaterRepository rp, String itemId, String userId, String rating){
        Rater r;
        if ((r = rp.findByUserAndItemAndRating(userId,itemId,rating)) == null){
            rp.save(new Rater(userId, itemId, rating));


            similarityManager.update(userId);

            suggestionsManager.update(userId);

        }else {
            if (r.getRating().equals(rating)){
                rp.save(new Rater(userId, itemId, rating));

                similarityManager.update(userId);

                suggestionsManager.update(userId);
            }
        }
    }

    public void remove(RaterRepository rp, String itemId, String userId, String rating){
        Rater r;
        if (( r = rp.findByUserAndItemAndRating(userId, itemId, rating)) != null){
            rp.delete(r);

            similarityManager.update(userId);

            suggestionsManager.update(userId);
        }
    }

}
