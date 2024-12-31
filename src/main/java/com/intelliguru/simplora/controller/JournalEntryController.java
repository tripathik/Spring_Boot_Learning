package com.intelliguru.simplora.controller;

import com.intelliguru.simplora.entity.JournalEntry;
import com.intelliguru.simplora.entity.User;
import com.intelliguru.simplora.service.JournalEntryService;
import com.intelliguru.simplora.service.UserService;
import com.intelliguru.simplora.utils.SecurityContextUtility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/journal")
@RestController
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntryOfUserName() {
        String userName = SecurityContextUtility.getAuthenticatedUser();
        User user = userService.findByUserName(userName);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if (allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            String userName = SecurityContextUtility.getAuthenticatedUser();
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myId) {
        String userName = SecurityContextUtility.getAuthenticatedUser();
        User user = userService.findByUserName(userName);
        List<JournalEntry> result = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();

        if (!result.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getEntriesById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId) {
        String userName = SecurityContextUtility.getAuthenticatedUser();
        boolean removed = journalEntryService.deleteEntryById(myId, userName);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {
        String userName = SecurityContextUtility.getAuthenticatedUser();
        User user = userService.findByUserName(userName);
        List<JournalEntry> result = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if (!result.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getEntriesById(myId);
            if (journalEntry.isPresent()) {
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent((newEntry.getContent() != null && !newEntry.getContent().isEmpty()) ? newEntry.getContent() : oldEntry.getContent());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        // For more clean code we can use a dependency commons-lang3.

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
