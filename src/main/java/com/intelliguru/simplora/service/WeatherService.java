package com.intelliguru.simplora.service;

import com.intelliguru.simplora.api.response.WeatherResponse;
import com.intelliguru.simplora.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    public WeatherResponse getWeather(String city){
        String weatherApi = appCache.APP_CACHE.get("weather_api").replace("<city>",city)
                .replace("<api_key>",apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(weatherApi, HttpMethod.POST, null, WeatherResponse.class);
        return response.getBody();
    }
}
