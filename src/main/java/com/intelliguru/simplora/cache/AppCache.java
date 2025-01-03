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

    private Map<String, String> cacheDataMap;
    @PostConstruct
    public boolean init(){
        boolean isReset = false;
        cacheDataMap = new HashMap<>();
        List<ConfigEntity> all = configRepository.findAll();
        for (ConfigEntity configEntity : all){
            cacheDataMap.put(configEntity.getKey(), configEntity.getValue());
        }

        if(!cacheDataMap.isEmpty()){
            isReset = true;
        }
        return isReset;
    }
    public String getApiValue(String key){
        return cacheDataMap.get(key);
    }
}
