package com.intelliguru.simplora.service;

import com.intelliguru.simplora.entity.User;
import com.intelliguru.simplora.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUserName() {
        User user = userRepository.findByUserName("Tripti");
        assertTrue(!user.getJournalEntries().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "2, 2, 4",
            "2, 4, 6"
    })
    void testParameterized(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }

    @ParameterizedTest
    @CsvSource({
            "Tripti",
            "Akshita",
            "Krishna"
    })
    void testGetByName_Parameterized(String name){
        assertNotNull(userRepository.findByUserName(name),"Failed for :"+name);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Tripti",
            "Akshita"
    })
    void testGetByName_Parameterized_ByValueSource(String name){
        assertNotNull(userRepository.findByUserName(name),"Failed for :"+name);
    }
}
