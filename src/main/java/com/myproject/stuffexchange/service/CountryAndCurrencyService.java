package com.myproject.stuffexchange.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.stuffexchange.model.CountryAndCurrency;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.ManyToOne;
import java.util.*;

@Service
public class CountryAndCurrencyService {

    public JsonNode getCountriesAndCurrencies(String path, RestTemplate restTemplate, ObjectMapper objectMapper) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(path,String.class);
        return objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));
    }

    public List<CountryAndCurrency> findCountryNames(String path, RestTemplate restTemplate, ObjectMapper objectMapper) throws JsonProcessingException {
        List<CountryAndCurrency> countryAndCurrencies = new ArrayList<>();
        JsonNode node =  getCountriesAndCurrencies(path,restTemplate,objectMapper);
        for (JsonNode jsonNode : node) {
            CountryAndCurrency countryAndCurrency = new CountryAndCurrency();
            countryAndCurrency.setCountryName(jsonNode.findValue("name").asText());
            countryAndCurrency.setCurrencyName(jsonNode.findValues("currencies").get(0).get(0).get("name").asText());
            countryAndCurrency.setCurrencyCode(jsonNode.findValues("currencies").get(0).get(0).get("code").asText());
            countryAndCurrencies.add(countryAndCurrency);
        }
        return countryAndCurrencies;
    }
}
