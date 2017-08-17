package org.recommend.recommendbasic.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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


    /**
     * This method updates the recommended item for a given user.
     * @param user represents the user whose recommendations are to be updated.
     */
    public void update(String user){

        // Retrieve the given user's likes, dislikes and other users with similar tastes.
        List<Rater> rlikes = raterRepository.findByUserAndRating(user, RatingManager.LIKE);
        List<Rater> rdislikes = raterRepository.findByUserAndRating(user, RatingManager.DISLIKE);
        Similars userSimilars = similarsRepository.findByUser(user);

        // Replace null values indicating no values with empty lists.
        if (rlikes == null){
            rlikes = new ArrayList<>(0);
        }
        if (rdislikes == null){
            rdislikes = new ArrayList<>(0);
        }

        List<String> likes = rlikes.stream().map(Rater::getItem).collect(Collectors.toList());
        List<String> dislikes = rdislikes.stream().map(Rater::getItem).collect(Collectors.toList());

        // Because this is a collaborative filtering method if there are no similars
        // recommendations will not be made.
        if (userSimilars == null){
            return;
        }

        // Get the similars
        HashMap<String, Double> others = userSimilars.getOthers();

        HashSet<String> itemsLikes = new HashSet<>();
        HashSet<String> itemsDislikes = new HashSet<>();

        // Retrieve all the likes and the dislikes of each of the other users.
        for (String other :
                others.keySet()) {

            // If this other person is the same as the given user move one.
            if (other.equals(user))
                continue;

            List<Rater> tempLike = raterRepository.findByUserAndRating(other, RatingManager.LIKE);
            List<Rater> tempDislike = raterRepository.findByUserAndRating(other, RatingManager.DISLIKE);

            if (tempLike == null) {
                tempLike = new ArrayList<>(0);
            }
            if (tempDislike == null) {
                tempDislike = new ArrayList<>(0);
            }


            itemsLikes.addAll(tempLike.stream().map(Rater::getItem).collect(Collectors.toList()));
            itemsDislikes.addAll(tempDislike.stream().map(Rater::getItem).collect(Collectors.toList()));

        }

        // Determine which items have not be rated by the given user
        itemsDislikes.removeAll(dislikes);
        itemsLikes.removeAll(likes);

        HashSet<String> unratedByUser = new HashSet<>();
        unratedByUser.addAll(itemsDislikes);
        unratedByUser.addAll(itemsLikes);

        // Storage for item and its weight (-1.0 - 1.0)
        HashMap<String, Double> itemsWithWeight = new HashMap<>();

        //Delete older recommendations
        suggestionsRepository.deleteByUser(user);

        // Find out how likely it is for the given user to like the unrated items
        for (String unratedItem :
                unratedByUser) {
            List<Rater> rlikers = raterRepository.findByItemAndRating(unratedItem, RatingManager.LIKE);
            List<Rater> rdislikers = raterRepository.findByItemAndRating(unratedItem, RatingManager.DISLIKE);

            // Null check
            if (rlikers == null) {
                rlikers = new ArrayList<>(0);
            }
            if (rdislikers == null) {
                rdislikers = new ArrayList<>(0);
            }

            List<String> likers = rlikers.stream().map(Rater::getItem).collect(Collectors.toList());
            List<String> dislikers = rdislikers.stream().map(Rater::getItem).collect(Collectors.toList());


            double similarityForLikers = 0;
            double similarityForDislikers = 0;

            for (String liker :
                    likers) {
                if (others.containsKey(liker)){
                    similarityForLikers += others.get(liker);
                }
            }
            for (String disliker :
                    dislikers) {
                if (others.containsKey(disliker)){
                    similarityForDislikers += others.get(disliker);
                }
            }

            HashSet<String> unionOfAll = new HashSet<>();
            unionOfAll.addAll(likers);
            unionOfAll.addAll(dislikers);

            double weight = (similarityForLikers-similarityForDislikers)/ (unionOfAll.size() * 1.0);

            itemsWithWeight.put(unratedItem, weight);

        }


        suggestionsRepository.save(new Suggestions(user, itemsWithWeight));

    }
}
