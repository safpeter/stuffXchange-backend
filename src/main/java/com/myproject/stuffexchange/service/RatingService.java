package com.myproject.stuffexchange.service;

import com.myproject.stuffexchange.data.UserRepository;
import com.myproject.stuffexchange.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    UserRepository userRepository;

     public void calculateAverageRating(String username, double rating) {
        AppUser appUser = userRepository.getAppUserByName(username);
        int numberOfRating = appUser.getNumberOfRating() + 1;
        double averageRating = (appUser.getRating() + rating) / numberOfRating;
        appUser.setNumberOfRating(numberOfRating);
        appUser.setRating(averageRating);
        userRepository.saveAndFlush(appUser);
    }

}
