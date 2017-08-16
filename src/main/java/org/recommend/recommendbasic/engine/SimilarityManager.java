package org.recommend.recommendbasic.engine;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by sheamus on 8/15/2017.
 */
public class SimilarityManager {

    @Autowired
    private SimilarsRepository similarsRepository;
    @Autowired
    private RaterRepository raterRepository;

    public void update(String userId){
        List<Rater> userLikes = raterRepository.findByUserAndRating(userId, "like");

        List<Rater> userDislikes = raterRepository.findByUserAndRating(userId, "dislike");

        Set<Rater> others = new LinkedHashSet<>();

        HashMap<String, Double> o = new HashMap<>();

        for (Rater r :
                userLikes) {
            others.addAll(raterRepository.findByItem(r.getItem()));
        }

        for (Rater r :
                userDislikes) {
            others.addAll(raterRepository.findByItem(r.getItem()));
        }

        for (Rater other :
                others) {
            o.put(other.getUser(),getSimilarity(other, userDislikes, userLikes));
        }

        Similars userSimilars = similarsRepository.findByUser(userId);
        userSimilars.setOthers(o);
        similarsRepository.save(userSimilars);


    }

    private double getSimilarity(Rater r, List<Rater> userDislikes, List<Rater> userLikes) {
        List<Rater> otherLikes = raterRepository.findByUserAndRating(r.getUser(), "like");
        List<Rater> otherDislikes = raterRepository.findByUserAndRating(r.getUser(), "dislike");

        Set<Rater> unionAll = new HashSet<>();
        unionAll.addAll(userLikes);
        unionAll.addAll(userDislikes);
        unionAll.addAll(otherLikes);
        unionAll.addAll(otherDislikes);

        List<Rater> listInterceptLikes = new ArrayList<>(userLikes);
        listInterceptLikes.retainAll(otherLikes);
        List<Rater> listInterceptDisLikes  = new ArrayList<>(userDislikes);
        listInterceptDisLikes.retainAll(otherDislikes);
        List<Rater> listInterceptLikesOtherDislikes  = new ArrayList<>(userLikes);
        listInterceptLikesOtherDislikes.retainAll(otherDislikes);
        List<Rater> listInterceptOthersLikesDislikes = new ArrayList<>(otherLikes);
        listInterceptOthersLikesDislikes.retainAll(userDislikes);

        double similarity = ((listInterceptLikes.size())+(listInterceptDisLikes.size())-(listInterceptLikesOtherDislikes.size())-(listInterceptOthersLikesDislikes.size()))/unionAll.size();

        return similarity;
    }


}
