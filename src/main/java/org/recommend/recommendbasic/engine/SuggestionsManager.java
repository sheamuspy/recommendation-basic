package org.recommend.recommendbasic.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sheamus on 8/15/2017.
 */
@Component
public class SuggestionsManager {

    @Autowired
    private RaterRepository raterRepository;
    @Autowired
    private SimilarsRepository similarsRepository;
    @Autowired
    private SuggestionsRepository suggestionsRepository;


    public void update(String userId){
        List<Rater> likes = raterRepository.findByUserAndRating(userId, RatingManager.LIKE);
        List<Rater> dislikes = raterRepository.findByUserAndRating(userId, RatingManager.DISLIKE);

        Similars userSimilars = similarsRepository.findByUser(userId);

        HashMap<String, Double> others = userSimilars.getOthers();

        HashSet<Rater> itemsLikes = new HashSet<>();
        HashSet<Rater> itemsDislikes = new HashSet<>();

        for (String other :
                others.keySet()) {
            itemsLikes.addAll(raterRepository.findByUserAndRating(other, RatingManager.LIKE));
            itemsDislikes.addAll(raterRepository.findByUserAndRating(other, RatingManager.DISLIKE));

        }

        itemsDislikes.removeAll(dislikes);
        itemsLikes.removeAll(likes);

        HashSet<Rater> unratedByUser = new HashSet<>();
        unratedByUser.addAll(itemsDislikes);
        unratedByUser.addAll(itemsLikes);

        HashMap<String, Double> itemsWithWeight = new HashMap<>();

        //TODO delete older recommendation
        suggestionsRepository.deleteByUser(userId);

        for (Rater r :
                unratedByUser) {
            List<Rater> likers = raterRepository.findByItemAndRating(r.getItem(), RatingManager.LIKE);
            List<Rater> dislikers = raterRepository.findByItemAndRating(r.getItem(), RatingManager.DISLIKE);

            double similarityForLikes = 0;
            double similarityForDislikes = 0;

            for (Rater liker :
                    likers) {
                if (others.containsKey(liker.getUser())){
                    similarityForLikes += others.get(liker.getUser());
                }
            }
            for (Rater disliker :
                    dislikers) {
                if (others.containsKey(disliker.getUser())){
                    similarityForDislikes += others.get(disliker.getUser());
                }
            }

            HashSet unionOfAll = new HashSet();
            unionOfAll.addAll(likers);
            unionOfAll.addAll(dislikers);

            double weight = (similarityForLikes-similarityForDislikes)/ unionOfAll.size();

            itemsWithWeight.put(r.getItem(), weight);

        }


        suggestionsRepository.save(new Suggestions(userId, itemsWithWeight));

    }
}
