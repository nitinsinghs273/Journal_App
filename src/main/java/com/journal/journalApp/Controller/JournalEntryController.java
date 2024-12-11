package com.journal.journalApp.Controller;


import com.journal.journalApp.Entities.JournalEntry;
import com.journal.journalApp.Service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired private JournalEntryService journalEntryService;

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntryOfUser(@RequestBody JournalEntry journalEntry) {
        return journalEntryService.createJournalEntryOfUser(journalEntry);
    }

    @GetMapping // Unique path for fetching all journal entries of a user
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser() {
        return journalEntryService.getAllJournalEntriesOfUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable Long id){
        return journalEntryService.getJournalEntryById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJournalEntryById(@PathVariable Long id){
        return journalEntryService.deleteJournalEntryById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(
            @PathVariable Long id,
            @RequestBody JournalEntry entry
    ){
        return journalEntryService.updateJournalEntryById(id, entry);
    }

}
