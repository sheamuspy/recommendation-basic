package org.recommend.recommendbasic.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by sheamus on 8/15/2017.
 */
@Component
public class RecommendationManager {

    @Autowired
    private RaterRepository raterRepository;
    @Autowired
    private SimilarityRepository similarityRepository;
    @Autowired
    private RecommendationRepository recommendationRepository;


    /**
     * This method updates the recommended item for a given user.
     * @param user represents the user whose recommendations are to be updated.
     */
    public void update(String user){

        // Retrieve the given user's likes, dislikes and other users with similar tastes.
        List<Rater> rlikes = raterRepository.findByUserAndRating(user, RatingManager.LIKE);
        List<Rater> rdislikes = raterRepository.findByUserAndRating(user, RatingManager.DISLIKE);
        List<Similarity> similarityPairs = similarityRepository.findByUser(user);

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
        if (similarityPairs == null){
            return;
        }

        HashSet<String> itemsLikes = new HashSet<>();
        HashSet<String> itemsDislikes = new HashSet<>();

        // Retrieve all the likes and the dislikes of each of the other users.
        for (Similarity similarityPair :
                similarityPairs) {

            // If this other person is the same as the given user move one.
            if (similarityPair.getOther().equals(user))
                continue;

            List<Rater> tempLike = raterRepository.findByUserAndRating(similarityPair.getOther(), RatingManager.LIKE);
            List<Rater> tempDislike = raterRepository.findByUserAndRating(similarityPair.getOther(), RatingManager.DISLIKE);

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

        //Delete older recommendations
        recommendationRepository.deleteByUser(user);

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

            List<String> likers = rlikers.stream().map(Rater::getUser).collect(Collectors.toList());
            List<String> dislikers = rdislikers.stream().map(Rater::getUser).collect(Collectors.toList());


            double similarityForLikers = 0;
            double similarityForDislikers = 0;

            for (String liker :
                    likers) {
                Optional<Similarity> si = similarityPairs.stream().filter(s->s.getOther().equals(liker)).findFirst();

                if (si.isPresent()){

                    similarityForLikers += si.get().getSimilarityIndex();
                }
            }
            for (String disliker :
                    dislikers) {
                Optional<Similarity> si = similarityPairs.stream().filter(s->s.getOther().equals(disliker)).findFirst();
                if (si.isPresent()){
                    similarityForDislikers += si.get().getSimilarityIndex();
                }
            }

            HashSet<String> unionOfAll = new HashSet<>();
            unionOfAll.addAll(likers);
            unionOfAll.addAll(dislikers);

            double weight = (similarityForLikers-similarityForDislikers)/ (unionOfAll.size() * 1.0);

            recommendationRepository.save(new Recommendation(user, unratedItem, weight));

        }




    }
}
