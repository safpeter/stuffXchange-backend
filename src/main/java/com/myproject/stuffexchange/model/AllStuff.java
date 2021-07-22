package com.myproject.stuffexchange.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllStuff {

    private long id;

    private String name;

    private double price;

    private String currency;

    private AppUser user;

    private long pictureId;

    private byte[] mainPicture;

}
