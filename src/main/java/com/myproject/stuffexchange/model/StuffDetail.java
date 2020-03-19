package com.myproject.stuffexchange.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuffDetail {

    private long id;

    private String name;

    private double price;

    private String currency;

    private LocalDate date;

    private String description;

    private AppUser user;

}
