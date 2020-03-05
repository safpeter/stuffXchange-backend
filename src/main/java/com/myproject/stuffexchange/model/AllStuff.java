package com.myproject.stuffexchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllStuff {


    private long id;

    private String name;

    private double price;

    private long pictureId;

    private byte[] mainPicture;
}
