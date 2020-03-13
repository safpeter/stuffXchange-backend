package com.myproject.stuffexchange.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.stuffexchange.data.CountryAndCurrencyRepository;
import com.myproject.stuffexchange.model.CountryAndCurrency;
import com.myproject.stuffexchange.service.CountryAndCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CountryAndCurrencyService countryAndCurrencyService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryAndCurrencyRepository countryAndCurrencyRepository;

    @Value("${countriesAndCurrenciesUrl}")
    String baseUrl;


    @Override
    public void run(String... args) throws Exception {
        if (countryAndCurrencyRepository.getAllCountries().size() == 1500){
            log.info("Nothing to Update");
        } else {
            List<CountryAndCurrency> countryAndCurrencies = countryAndCurrencyService.findCountryNames(baseUrl, restTemplate, objectMapper);
            countryAndCurrencyRepository.saveAll(countryAndCurrencies);
        }
    }



}
