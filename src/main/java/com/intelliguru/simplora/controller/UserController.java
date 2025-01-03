package com.intelliguru.simplora.controller;

import com.intelliguru.simplora.entity.User;
import com.intelliguru.simplora.exception.WeatherException;
import com.intelliguru.simplora.repository.UserRepository;
import com.intelliguru.simplora.service.UserService;
import com.intelliguru.simplora.service.WeatherService;
import com.intelliguru.simplora.utils.SecurityContextUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<?> greeting(@PathVariable String city) {
        try {
            String userName = SecurityContextUtility.getAuthenticatedUser();
            String weatherResponse = weatherService.getWeather(city, userName);
            return new ResponseEntity<>(weatherResponse, HttpStatus.OK);
        } catch (WeatherException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String userName = SecurityContextUtility.getAuthenticatedUser();
        User existingUser = userService.findByUserName(userName);
        existingUser.setUserName(user.getUserName());
        existingUser.setPassword(user.getPassword());
        userService.saveNewUser(existingUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping
    public ResponseEntity<?> deleteUserByUserName(){
        String userName = SecurityContextUtility.getAuthenticatedUser();
        userRepository.deleteByUserName(userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
