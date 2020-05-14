package com.myproject.stuffexchange.service;

import com.myproject.stuffexchange.data.UserRepository;
import com.myproject.stuffexchange.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RatingService {

    @Autowired
    UserRepository userRepository;

    public void setRating(String raterUser, String userToRate, double rating) {
        AppUser userWhoRate = userRepository.getAppUserByName(raterUser);
        AppUser userWhoIsRated = userRepository.getAppUserByName(userToRate);
        Map<AppUser, Double> ratings = userWhoRate.getMyRatings();
        double oneRating =  ratings.getOrDefault(userWhoIsRated, 0.0);
        int numberOfRating = userWhoIsRated.getNumberOfRating();
        double averageRating = userWhoIsRated.getRating();
        if (ratings.containsKey(userWhoIsRated)){
            double updatedAverageRating = (averageRating * numberOfRating -  oneRating + rating) / numberOfRating; //calculates new average if user has already rated
            userWhoIsRated.setRating(updatedAverageRating);
            ratings.replace(userWhoIsRated, rating);
        } else {
            ratings.put(userWhoIsRated, rating);
            int newNumberOdRating = numberOfRating + 1;
             userWhoIsRated.setNumberOfRating(newNumberOdRating);
            double newAverageRating =  rating / newNumberOdRating;
            userWhoIsRated.setRating(newAverageRating);
        }
        userRepository.saveAndFlush(userWhoIsRated);
        userWhoRate.setMyRatings(ratings);
        userRepository.saveAndFlush(userWhoRate);
    }



}
