package org.recommend.recommendbasic.engine;

/**
 * Created by sheamus on 8/14/2017.
 */
public class RatingManager {

    public void RatingManger(){}

    public void add(RaterRepository rp, String itemId, String userId, String rating){
        Rater r;
        if ((r = rp.findByUserItemAndRating(userId,itemId,rating)) == null){
            rp.save(new Rater(userId, itemId, rating));
        }else {
            if (r.getRating().equals(rating)){
                rp.save(new Rater(userId, itemId, rating));
            }
        }
    }

    public void remove(RaterRepository rp, String itemId, String userId, String rating){
        Rater r;
        if (( r = rp.findByUserItemAndRating(userId, itemId, rating)) != null){
            rp.delete(r);
        }
    }

}
