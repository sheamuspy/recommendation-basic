package org.recommend.recommendbasic;

import org.recommend.recommendbasic.engine.Rater;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by sheamus on 8/18/2017.
 */
public class RaterItemProcessor implements ItemProcessor<IRater, Rater>{

    @Override
    public Rater process(IRater rater) throws Exception {

        Rater temp = new Rater();
        temp.setUser(rater.getUser());
        temp.setItem(rater.getItem());
        temp.setRating(rater.getRating());

        return temp;
    }
}
