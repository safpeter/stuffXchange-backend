package com.myproject.stuffexchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CountryAndCurrency {

    @Id
    @GeneratedValue
    private long id;

    private String countryName;

    private String currencyName;

    private String currencyCode;
}
