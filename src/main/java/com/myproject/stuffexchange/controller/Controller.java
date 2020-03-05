package com.myproject.stuffexchange.controller;


import com.myproject.stuffexchange.data.ImageRepository;
import com.myproject.stuffexchange.data.StuffPropertyRepository;

import com.myproject.stuffexchange.model.*;
import com.myproject.stuffexchange.service.ImageTransformService;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@CrossOrigin
@RestController
public class Controller {

    @Autowired
    StuffPropertyRepository stuffPropertyRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageTransformService transformService;

    @GetMapping(value = "/getallstuff")
    public @ResponseBody List<AllStuffToUpload> getAllStuff() {
        List<AllStuff> allStuffs =   stuffPropertyRepository.getAllStuff();
        List<AllStuffToUpload> stuffToUpload = new ArrayList<>();
        for (AllStuff stuff : allStuffs) {
            AllStuffToUpload toUpload = AllStuffToUpload.builder()
                    .id(stuff.getId())
                    .name(stuff.getName())
                    .price(stuff.getPrice())
                    .mainPicture(transformService.transformByteArraysToString(stuff))
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
        System.out.println(images.get(0).getId());
        stuff.setMainPicture(images.get(0).getId());
        stuff.setImages(images);
        stuffPropertyRepository.saveAndFlush(stuff);
    }




}
