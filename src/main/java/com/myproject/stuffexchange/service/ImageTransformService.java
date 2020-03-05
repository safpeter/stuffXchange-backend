package com.myproject.stuffexchange.service;
import com.myproject.stuffexchange.model.AllStuff;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


@Service
public class ImageTransformService {


    public byte[] getByteArray(MultipartFile file) throws IOException {
        return  file.getBytes();
    }

    public List<byte[]> transformImages(MultipartFile[] files){
        List<byte[]> listOfByteArrays = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            try {
                byte[] bytes = getByteArray(file);
                listOfByteArrays.add(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return listOfByteArrays;
    }

    public String transformByteArraysToString(AllStuff stuff) {
        byte[] bytes = stuff.getMainPicture();
        String string = "data:image/png;base64," +  Base64.getEncoder().encodeToString(bytes);
        System.out.println(string);
         return  string;
        }
}
