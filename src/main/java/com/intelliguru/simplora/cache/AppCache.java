package com.intelliguru.simplora.cache;

import com.intelliguru.simplora.entity.ConfigEntity;
import com.intelliguru.simplora.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigRepository configRepository;

    public  final Map<String, String> APP_CACHE = new HashMap<>();
    @PostConstruct
    public void init(){
        List<ConfigEntity> all = configRepository.findAll();
        for (ConfigEntity configEntity : all){
            APP_CACHE.put(configEntity.getKey(), configEntity.getValue());
        }
    }
}
