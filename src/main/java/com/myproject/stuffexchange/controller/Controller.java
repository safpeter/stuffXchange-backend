package com.myproject.stuffexchange.controller;


import com.myproject.stuffexchange.data.ImageRepository;
import com.myproject.stuffexchange.data.StuffPropertyRepository;

import com.myproject.stuffexchange.model.AllStuff;
import com.myproject.stuffexchange.model.Image;
import com.myproject.stuffexchange.model.NewStuff;
import com.myproject.stuffexchange.model.StuffProperty;
import com.myproject.stuffexchange.service.ImageTransformService;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
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

    @GetMapping("/getallstuff")
    public Set<AllStuff> getAllStuff(){
       return stuffPropertyRepository.getAllStuff();
    }

    @PostMapping(value = "/uploadstuff")
    public void uploadStuff(@ModelAttribute("images") NewStuff newStuff) {
        System.out.println(newStuff.getName());
        System.out.println(newStuff.getPrice());
        System.out.println(newStuff.getDescription());
        System.out.println(Arrays.toString(newStuff.getImages()));
        List<Image> images = new ArrayList<>();
        List<byte[]> imagesInBytes  = transformService.transformImages(newStuff.getImages());
        StuffProperty stuff = StuffProperty.builder()
                .name(newStuff.getName())
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

        stuff.setImages(images);
        imageRepository.saveAll(images);
        stuffPropertyRepository.saveAndFlush(stuff);
        }




}
