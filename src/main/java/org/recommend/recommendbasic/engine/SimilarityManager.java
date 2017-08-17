package org.recommend.recommendbasic.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sheamus on 8/15/2017.
 */
@Component
public class SimilarityManager {

    @Autowired
    private SimilarsRepository similarsRepository;
    @Autowired
    private RaterRepository raterRepository;

    /**
     * This method updates the list of users that are similar to a given user.
     * @param user represents the user who list of similar users needs to be updated.
     */
    public void update(String user){

        // Retrieve all the items that the given user likes and dislikes.
        List<Rater> ruserLikes = raterRepository.findByUserAndRating(user, RatingManager.LIKE);
        List<Rater> ruserDislikes = raterRepository.findByUserAndRating(user, RatingManager.DISLIKE);

        if (ruserLikes == null){
            ruserLikes = new ArrayList<>(0);
        }
        if (ruserDislikes == null){
            ruserDislikes = new ArrayList<>(0);
        }

        List<String> userLikes = ruserLikes.stream().map(Rater::getItem).collect(Collectors.toList());
        List<String> userDislikes = ruserDislikes.stream().map(Rater::getItem).collect(Collectors.toList());

        // Set that will contain all other users that have rated an item rated by
        // the given user.
        Set<Rater> others = new LinkedHashSet<>();

        // Hash map to contain the other user and its similarity index.
        HashMap<String, Double> o = new HashMap<>();

        // Retrieve all other users that have rated an item liked by the given user
        for (String userLike :
                userLikes) {
            others.addAll(raterRepository.findByItem(userLike));
        }

        // Retrieve all other users that have rated an item disliked by the given user
        for (String userDislike :
                userDislikes) {
            others.addAll(raterRepository.findByItem(userDislike));
        }

        // If the there are others that have rated items rated by the given user then
        // for each of these users find their similarity index to the given user.
        if (!others.isEmpty()) {
            for (Rater other :
                    others) {

                if (other.getUser().equals(user))
                    continue;

                o.put(other.getUser(), getSimilarity(other, userDislikes, userLikes));
            }

            Similars userSimilars = similarsRepository.findByUser(user);

            // If no similars exists for the given user then create a new one.
            if (userSimilars == null){
                userSimilars =  new Similars(user);
            }
            userSimilars.setOthers(o);
            similarsRepository.save(userSimilars);
        }


    }

    /**
     *  This method calculates the similarity index for a user. That mean it calculates how
     *  similar two users are.
     * @param r represents the user whose similarity index is to be calculated.
     * @param userDislikes represents the dislikes of the base user.
     * @param userLikes represents the likes of the base user.
     * @return represents the similarity index.
     */
    private double getSimilarity(Rater r, List<String> userDislikes, List<String> userLikes) {

        // Retrieve the likes and dislikes of the other user.
        List<Rater> rotherLikes = raterRepository.findByUserAndRating(r.getUser(), RatingManager.LIKE);
        List<Rater> rotherDislikes = raterRepository.findByUserAndRating(r.getUser(),RatingManager.DISLIKE );

        // If either the likes or the dislikes of the other users come back with a null response,
        // replace it with an empty list.
        if (rotherLikes == null){
            rotherLikes = new ArrayList<>(0);
        }
        if (rotherDislikes == null){
            rotherDislikes = new ArrayList<>(0);
        }

        List<String> otherLikes = rotherLikes.stream().map(Rater::getItem).collect(Collectors.toList());
        List<String> otherDislikes = rotherDislikes.stream().map(Rater::getItem).collect(Collectors.toList());

        // Create a union set of both users likes and dislikes.
        HashSet<String> unionAll = new HashSet<>();
        unionAll.addAll(userLikes);
        unionAll.addAll(userDislikes);
        unionAll.addAll(otherLikes);
        unionAll.addAll(otherDislikes);

        // Find the items that both users liked.
        List<String> listInterceptLikes = new ArrayList<>(userLikes);
        listInterceptLikes.retainAll(otherLikes);

        // Find the items that both users disliked.
        List<String> listInterceptDisLikes  = new ArrayList<>(userDislikes);
        listInterceptDisLikes.retainAll(otherDislikes);

        // Find the items that the base user liked but the other user disliked
        List<String> listInterceptLikesOtherDislikes  = new ArrayList<>(userLikes);
        listInterceptLikesOtherDislikes.retainAll(otherDislikes);

        // Find the items that the base user disliked but the other user liked.
        List<String> listInterceptOthersLikesDislikes = new ArrayList<>(otherLikes);
        listInterceptOthersLikesDislikes.retainAll(userDislikes);

        // Calculate the similarity index
        double similarity = ((listInterceptLikes.size())+(listInterceptDisLikes.size())-(listInterceptLikesOtherDislikes.size())-(listInterceptOthersLikesDislikes.size()))/(unionAll.size() * 1.0);

        return similarity;
    }


}
