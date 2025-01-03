package com.intelliguru.simplora.service;

import com.intelliguru.simplora.api.response.WeatherResponse;
import com.intelliguru.simplora.cache.AppCache;
import com.intelliguru.simplora.exception.WeatherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.intelliguru.simplora.utils.constants.Constants.API_KEY;
import static com.intelliguru.simplora.utils.constants.Constants.CITY;
import static com.intelliguru.simplora.utils.constants.Keys.WEATHER_API;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    public String getWeather(String city, String userName) throws WeatherException {
        try {
            String weatherApi = appCache.getApiValue(WEATHER_API.toString());
            weatherApi = weatherApi.replace(CITY,city).replace(API_KEY,apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(weatherApi, HttpMethod.POST, null, WeatherResponse.class);
            WeatherResponse weatherResponse = response.getBody();
            String greeting = "";
            if(weatherResponse != null ){
                greeting = ", Weather of "+city+", feels like " + weatherResponse.getCurrent().getFeelslike();
            }
            return "Hi " + userName + greeting;
        }catch (Exception e){
            throw  new WeatherException("Some error occurred while fetching the weather Api details: ");
        }
    }
}
