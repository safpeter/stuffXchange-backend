package com.myproject.stuffexchange.controller;


import com.myproject.stuffexchange.data.CountryAndCurrencyRepository;
import com.myproject.stuffexchange.data.ImageRepository;
import com.myproject.stuffexchange.data.StuffPropertyRepository;

import com.myproject.stuffexchange.model.*;
import com.myproject.stuffexchange.service.ImageTransformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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

    @GetMapping(value = "/getallstuff")
    public @ResponseBody List<AllStuffToUpload> getAllStuff() {
        List<AllStuff> allStuffs =   stuffPropertyRepository.getAllStuff();
        List<AllStuffToUpload> stuffToUpload = new ArrayList<>();
        for (AllStuff stuff : allStuffs) {
            AllStuffToUpload toUpload = AllStuffToUpload.builder()
                    .id(stuff.getId())
                    .name(stuff.getName())
                    .price(stuff.getPrice())
                    .currency(stuff.getCurrency())
                    .mainPicture(transformService.getBytesFromStuff(stuff))
                    .build();

            stuffToUpload.add(toUpload);
        }
        return stuffToUpload;
    }


    @PostMapping(value = "/uploadstuff")
    public void uploadStuff(@ModelAttribute NewStuff newStuff) {
        List<Image> images = new ArrayList<>();
        List<byte[]> imagesInBytes  = transformService.transformImages(newStuff.getImages());
        StuffProperty stuff = StuffProperty.builder()
                .name(newStuff.getName())
                .date(LocalDate.now())
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


}
