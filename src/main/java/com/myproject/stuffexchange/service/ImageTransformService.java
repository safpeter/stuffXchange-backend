package com.myproject.stuffexchange.service;

import com.myproject.stuffexchange.model.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class ImageTransformService {

    public byte[] getByteArray(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    public List<byte[]> transformImages(MultipartFile[] files){
        List<byte[]> arrayOfByteArrays = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            try {
                byte[] bytes = getByteArray(file);
                arrayOfByteArrays.add(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return arrayOfByteArrays;
    }
}
