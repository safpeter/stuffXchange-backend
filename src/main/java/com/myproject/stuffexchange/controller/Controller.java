package com.myproject.stuffexchange.controller;


import com.myproject.stuffexchange.data.CountryAndCurrencyRepository;
import com.myproject.stuffexchange.data.ImageRepository;
import com.myproject.stuffexchange.data.StuffPropertyRepository;

import com.myproject.stuffexchange.data.UserRepository;
import com.myproject.stuffexchange.model.*;
import com.myproject.stuffexchange.service.ImageTransformService;
import com.myproject.stuffexchange.service.RatingService;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@CrossOrigin
@RestController
public class Controller {

    @Autowired
    StuffPropertyRepository stuffPropertyRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageTransformService transformService;

    @Autowired
    CountryAndCurrencyRepository countryAndCurrencyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatingService ratingService;

    @GetMapping(value = "/getalluserstuff/{username}")
    public @ResponseBody List<AllStuffToUpload> getAllUserStuff(@PathVariable("username") String username) {
        List<AllStuff> allStuffs =   stuffPropertyRepository.getAllUserStuff(username);
        List<AllStuffToUpload> UserStuffToUpload = new ArrayList<>();
        for (AllStuff stuff : allStuffs) {
            AllStuffToUpload toUpload = AllStuffToUpload.builder()
                    .id(stuff.getId())
                    .name(stuff.getName())
                    .price(stuff.getPrice())
                    .currency(stuff.getCurrency())
                    .user(stuff.getUser().getName())
                    .mainPicture(transformService.getBytesFromStuff(stuff))
                    .build();

            UserStuffToUpload.add(toUpload);
        }
        return UserStuffToUpload;
    }

    @GetMapping(value = "/getallfavouritestuff/{username}")
    public @ResponseBody List<AllStuffToUpload> getAllFavouriteStuff(@PathVariable("username") String username) {
       List<AllStuff> allStuffs =   stuffPropertyRepository.getAllFavouriteStuff(username);
        List<AllStuffToUpload> favouriteStuffToUpload = new ArrayList<>();
        for (AllStuff stuff : allStuffs) {
            AllStuffToUpload toUpload = AllStuffToUpload.builder()
                    .id(stuff.getId())
                    .name(stuff.getName())
                    .price(stuff.getPrice())
                    .currency(stuff.getCurrency())
                    .user(stuff.getUser().getName())
                    .mainPicture(transformService.getBytesFromStuff(stuff))
                    .build();

            favouriteStuffToUpload.add(toUpload);
        }
        return favouriteStuffToUpload;
    }


    @PostMapping(value = "/uploadstuff")
    public void uploadStuff(@ModelAttribute NewStuff newStuff) {
        List<Image> images = new ArrayList<>();
        List<byte[]> imagesInBytes  = transformService.transformImages(newStuff.getImages());
        AppUser appUser = userRepository.getAppUserByName(newStuff.getUsername());
        StuffProperty stuff = StuffProperty.builder()
                .name(newStuff.getName())
                .date(LocalDate.now())
                .user(appUser)
                .price(newStuff.getPrice())
                .currency(newStuff.getCurrency())
                .description(newStuff.getDescription())
                .build();

        for (int i = 0; i < imagesInBytes.size() ; i++) {
            Image image = Image.builder()
                    .image(imagesInBytes.get(i))
                    .stuffProperty(stuff)
                    .build();
            images.add(image);
        }

        imageRepository.saveAll(images);
        stuff.setMainPicture(images.get(0).getId());
        stuff.setImages(images);
        stuffPropertyRepository.saveAndFlush(stuff);
    }

    @GetMapping("/stuffdetails/{id}")
    public StuffDetail getStuffDetails(@PathVariable("id") long id){
        return stuffPropertyRepository.getStuffDetails(id);
    }

    @GetMapping("/getimages/{id}")
    public List<String> getImages(@PathVariable("id") long id){
        StuffProperty stuffProperty = stuffPropertyRepository.getById(id);
        List<Image> images =  imageRepository.getImagesByStuffProperty(stuffProperty);
        List<String> strings = new ArrayList<>();
        for (Image image : images) {
           String string = transformService.getBytesFromImage(image);
           strings.add(string);
        }
        return strings;
    }

    @GetMapping("/getcountries")
    public List<String> getAllCountries(){
        return countryAndCurrencyRepository.getAllCountries();
    }

    @GetMapping("/getcurrencies")
    public List<String> getAllCurrencies(){
        return countryAndCurrencyRepository.getAllCurrencyCode();
    }

    @GetMapping("/getuserdetails/{username}")
    public AppUser getUserDetails(@PathVariable("username") String username){
        AppUser user = userRepository.getAppUserByName(username);
        return user;
    }

    @DeleteMapping("/deletestuff/{id}")
    public boolean deleteStuff(@PathVariable("id") long id) {
        imageRepository.deleteImagesByStuffPropertyId(id);
        stuffPropertyRepository.deleteStuffPropertyById(id);
        return true;
    }

    @GetMapping("/getsearch/{search}")
    public List<AllStuffToUpload> getSearches(@PathVariable("search") String search){
        List<AllStuff> allStuffs = stuffPropertyRepository.searchStuff(search);
        List<AllStuffToUpload> allStuffToUpload = new ArrayList<>();
        for (AllStuff stuff : allStuffs) {
           AllStuffToUpload stuffToUpload = AllStuffToUpload.builder()
                    .id(stuff.getId())
                    .name(stuff.getName())
                    .price(stuff.getPrice())
                    .currency(stuff.getCurrency())
                    .mainPicture(transformService.getBytesFromStuff(stuff))
                    .user(stuff.getUser().getName())
                   .build();

           allStuffToUpload.add(stuffToUpload);
        }
        return allStuffToUpload;
    }

    @PostMapping("/markasfavourite")
    public boolean markAsFavourite(@RequestBody Favourite favourite){
        AppUser user = userRepository.getAppUserByName(favourite.getUsername());
        StuffProperty stuff = stuffPropertyRepository.getById(favourite.getStuffId());
        Set<StuffProperty> stuffs = new HashSet<>();
        stuffs.add(stuff);
        user.setFavouriteStuffs(stuffs);
        userRepository.saveAndFlush(user);
        return  true;
    }

    @PostMapping("/rateuser")
    public void rateUser(@RequestBody NewRating newRating){
        System.out.println(newRating.getUserName());
        System.out.println(newRating.getRating());
        ratingService.calculateAverageRating(newRating.getUserName(), newRating.getRating() );
    }



}
