package com.myproject.stuffexchange.data;

import com.myproject.stuffexchange.model.CountryAndCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountryAndCurrencyRepository extends JpaRepository<CountryAndCurrency,Long> {

    @Query("select e.countryName from CountryAndCurrency e")
    List<String> getAllCountries();

    @Query("select e.currencyCode from  CountryAndCurrency e")
    List<String> getAllCurrencyCode();



}
