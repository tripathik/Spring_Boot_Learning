package com.intelliguru.simplora.service;

import com.intelliguru.simplora.entity.User;
import com.intelliguru.simplora.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNewUser() {
        User user = new User("test","plainPassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.saveNewUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUser() {
        User user = new User("test","plainPassword");
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User("test","plainPassword"), new User("test","plainPassword"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        ObjectId id = new ObjectId();
        User user = new User("test","plainPassword");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteUserById() {
        ObjectId id = new ObjectId();

        userService.deleteUserById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByUserName() {
        String userName = "testUser";
        User user = new User("test","plainPassword");
        when(userRepository.findByUserName(userName)).thenReturn(user);

        User result = userService.findByUserName(userName);

        assertEquals(user, result);
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    void testSaveAdmin() {
        User user = new User("test","plainPassword");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        userService.saveAdmin(user);

        verify(userRepository, times(1)).save(user);
    }
}
