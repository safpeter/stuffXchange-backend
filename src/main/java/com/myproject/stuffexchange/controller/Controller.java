package com.myproject.stuffexchange.controller;


import com.myproject.stuffexchange.data.CountryAndCurrencyRepository;
import com.myproject.stuffexchange.data.ImageRepository;
import com.myproject.stuffexchange.data.StuffPropertyRepository;

import com.myproject.stuffexchange.data.UserRepository;
import com.myproject.stuffexchange.model.*;
import com.myproject.stuffexchange.service.ImageTransformService;
import com.myproject.stuffexchange.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;


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

    @GetMapping("/getallfavouritestuff/{username}")
    public List<AllStuffToUpload> getFav(@PathVariable("username") String username){
        AppUser user = userRepository.getAppUserByName(username);
        List<StuffProperty> favouriteStuffs = user.getMyFavourites();
        List<Long> favouriteStuffIds = new ArrayList<>();
        for (StuffProperty favouriteStuff : favouriteStuffs) {
            favouriteStuffIds.add(favouriteStuff.getId());
        }
        List<AllStuff> favourites  = stuffPropertyRepository.getFavouriteStuff(favouriteStuffIds);
        List<AllStuffToUpload> favouritesToUpload = new ArrayList<>();
        for (AllStuff favouriteStuff : favourites) {
            AllStuffToUpload stuffToUpload = AllStuffToUpload.builder()
                    .id(favouriteStuff.getId())
                    .user(favouriteStuff.getUser().getName())
                    .mainPicture(transformService.getBytesFromStuff(favouriteStuff))
                    .price(favouriteStuff.getPrice())
                    .currency(favouriteStuff.getCurrency())
                    .name(favouriteStuff.getName())
                    .build();

            favouritesToUpload.add(stuffToUpload);
        }
        return favouritesToUpload;
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

        for (byte[] imagesInByte : imagesInBytes) {
            Image image = Image.builder()
                    .image(imagesInByte)
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
    public AppUser getUserDetails(@PathVariable("username") String username) {
        return userRepository.getAppUserByName(username);
    }

    @DeleteMapping("/deletestuff/{id}/{username}")
    public boolean deleteStuff(@PathVariable("id") long id,@PathVariable("username") String username) {
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
    public void markAsFavourite(@RequestBody NewFavourite favourite){
        AppUser user = userRepository.getAppUserByName(favourite.getUsername());
        StuffProperty stuffProperty = stuffPropertyRepository.getById(favourite.getStuffId());
        List<StuffProperty> favouriteStuffs = user.getMyFavourites();
        favouriteStuffs.add(stuffProperty);
        user.setMyFavourites(favouriteStuffs);
        userRepository.saveAndFlush(user);
    }

    @PostMapping("/rateuser")
    public void rateUser(@RequestBody NewRating newRating){
        ratingService.setRating(newRating.getRaterUserName(),newRating.getUserName(),newRating.getRating());
    }

    @PutMapping("/updateprofile/{id}")
    public AppUser updateProfile(@PathVariable("id") long id, @RequestBody NewUser newUser) {
        System.out.println(newUser);
        AppUser userToUpdate = userRepository.getAppUserById(id);
        if(newUser.getName() != null   ){
            userToUpdate.setName(newUser.getName());
        }
        if(newUser.getEmail() != null  ){
            userToUpdate.setEmail(newUser.getEmail());
        }
        if(newUser.getCountry() != null ){
            userToUpdate.setCountry(newUser.getCountry());
        }

        userRepository.saveAndFlush(userToUpdate);

        return  userToUpdate;
    }

    @GetMapping("/getpopularstuff/{username}")
    public @ResponseBody List<AllStuffToUpload> getPopularStuff(@PathVariable("username") String username) {
        List<AllStuff> allStuffs =   stuffPropertyRepository.getPopularStuff(username);
        List<AllStuffToUpload> popularStuffs = new ArrayList<>();
        for (AllStuff stuff : allStuffs) {
            AllStuffToUpload toUpload = AllStuffToUpload.builder()
                    .id(stuff.getId())
                    .name(stuff.getName())
                    .price(stuff.getPrice())
                    .currency(stuff.getCurrency())
                    .user(stuff.getUser().getName())
                    .mainPicture(transformService.getBytesFromStuff(stuff))
                    .build();
            popularStuffs.add(toUpload);
        }

        return popularStuffs;
    }

    @DeleteMapping("/deleteprofile/{id}")
    public boolean deleteProfile(@PathVariable("id") long id){
        userRepository.deleteById(id);
        return userRepository.existsById(id);

    }


    @PutMapping("/updatestuff/{id}")
    public void updateStuff(@PathVariable("id") long id, @ModelAttribute NewStuff newStuff) {
        System.out.println(newStuff);
        StuffProperty stuffToUpdate = stuffPropertyRepository.getById(id);
            stuffToUpdate.setName(newStuff.getName());
            stuffToUpdate.setCurrency(newStuff.getCurrency());
            stuffToUpdate.setPrice(newStuff.getPrice());
            stuffToUpdate.setDescription(newStuff.getDescription());

        if( newStuff.getImages() != null) {
            List<Image> images = new ArrayList<>();
            List<byte[]> imagesInBytes = transformService.transformImages(newStuff.getImages());
            for (byte[] imagesInByte : imagesInBytes) {
                Image image = Image.builder()
                        .image(imagesInByte)
                        .stuffProperty(stuffToUpdate)
                        .build();
                images.add(image);

                imageRepository.saveAll(images);
                stuffToUpdate.setImages(images);
            }
        }

        stuffPropertyRepository.saveAndFlush(stuffToUpdate);
    }




}
