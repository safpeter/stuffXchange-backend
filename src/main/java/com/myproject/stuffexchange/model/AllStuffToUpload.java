package com.myproject.stuffexchange.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllStuffToUpload {

    private long id;

    private String name;

    private double price;

    private String currency;

    private String mainPicture;
}
