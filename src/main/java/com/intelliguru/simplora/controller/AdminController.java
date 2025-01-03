package com.intelliguru.simplora.controller;

import com.intelliguru.simplora.cache.AppCache;
import com.intelliguru.simplora.entity.User;
import com.intelliguru.simplora.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RestController
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppCache appCache;

    @GetMapping("clear-cache")
    public ResponseEntity<?> resetCache(){
        boolean reset = appCache.init();
        if(reset){
            return new ResponseEntity<>("The cache Value has been successfully invalidated.", HttpStatus.OK);
        }
        log.error("Some error occurred while invalidating the cache.");
        throw new RuntimeException("Some error occurred while invalidating the cache.");
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();

        if(ObjectUtils.isNotEmpty(allUsers)){
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>("There are no any User Records found.", HttpStatus.NOT_FOUND);
    }
    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        userService.saveAdmin(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
