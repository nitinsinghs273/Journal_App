package com.journal.journalApp.Service;

import com.journal.journalApp.Entities.JournalEntry;
import com.journal.journalApp.Entities.User;
import com.journal.journalApp.RepositoryDao.JournalEntryRepository;
import com.journal.journalApp.RepositoryDao.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JournalEntryService {

    @Autowired private JournalEntryRepository journalEntryRepository;

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;


    @Transactional
    public ResponseEntity<JournalEntry> createJournalEntryOfUser(JournalEntry journalEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.getUserByName(username).getBody();
            if(user == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            //user.setUsername(null);//using Transactional to avoid any irregularities in the databases
            journalEntry.setUser(user);//
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            System.out.println(user);
            userService.updateUser(user);


            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user =  userService.getUserByName(username).getBody();
            if (user.getJournalEntries() != null && !user.getJournalEntries().isEmpty()) {
                return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<JournalEntry> getJournalEntryById(Long id) {
      try{
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          String username = authentication.getName();
          List<JournalEntry> journalEntries = userService.findByUserName(username).getJournalEntries();

          // Filter for the journal entry with the given ID
         List<JournalEntry> entryOfUser = journalEntries.stream().filter(entry -> entry.getId().equals(id)).collect(Collectors.toList());

          if(!entryOfUser.isEmpty()){
              return new ResponseEntity<>(entryOfUser.getFirst(), HttpStatus.OK);
          }
          else{
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
      }catch(Exception e){
              e.printStackTrace();
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }
    }

    @Transactional
    public ResponseEntity<String> deleteJournalEntryById(Long id) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            boolean removed = user.getJournalEntries().removeIf(journal ->journal.getId().equals(id));
            if(removed){
               userRepository.save(user);
                return new ResponseEntity<>("Deletion successful", HttpStatus.NO_CONTENT);
            }else{
                return new ResponseEntity<>("Entry Not Found", HttpStatus.NOT_FOUND);

            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<JournalEntry> updateJournalEntryById(Long id, JournalEntry entry ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            List<JournalEntry> existingEntrys = user.getJournalEntries().stream().filter(entryss ->entryss.getId().equals(id)).collect(Collectors.toList());

            if(!existingEntrys.isEmpty()){
                Optional<JournalEntry> existingEntry = existingEntrys.stream().findFirst();
                if(existingEntry.isPresent()){
                    existingEntry.get().setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle(): existingEntry.get().getTitle());
                    existingEntry.get().setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent(): existingEntry.get().getContent());
                    existingEntry.get().setDate(entry.getDate());
                    existingEntry.get().setAuthor(entry.getAuthor());
                    journalEntryRepository.save(existingEntry.get());
                    return new ResponseEntity<>(existingEntry.get(), HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("hello");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }


}
