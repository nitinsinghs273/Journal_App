package com.journal.journalApp.Service;


import com.journal.journalApp.Entities.User;

import com.journal.journalApp.RepositoryDao.UserRepository;
import com.journal.journalApp.dto.WheatherApiResponseDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  WeatherService weatherService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    ///pasword:"U95jXYNptP@Zfqd"





    public ResponseEntity<User> createUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // encode password before saving it to the database.
            user.setRoles(Arrays.asList("ADMIN"));
            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error happened", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            if (!users.isEmpty() && users != null) {
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<String> GreetUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            WheatherApiResponseDTO response =  weatherService.getWeather(user.getCity()).getBody();
            return new ResponseEntity<>("Hello, " + username + " Weather in " + user.getCity() + " is " +  response.getCurrent().getTemperature() + " degree celsius but feels like " + response.getCurrent().feelslike , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Hello, Guest Weather is Nice!", HttpStatus.OK);
        }
    }

    public ResponseEntity<User> getUserByName(String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> deleteJournalEntryById(Long id) {
        try {
            Optional<User> deletedUser = userRepository.findById(id);
            if (deletedUser.isPresent() && deletedUser != null) {
                userRepository.deleteById(id);
                return new ResponseEntity<>(deletedUser.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> updateUser(User updatedUser) {
        try {
            // Get the currently authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Fetch the existing user from the database
            User existingUser = userRepository.findByUsername(username);
            if (existingUser != null) {
                // Update allowed fields
                existingUser.setUsername(updatedUser.getUsername());

                // Update and encode the password only if it's not blank
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank() && !updatedUser.getPassword().equals(existingUser.getPassword())) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }
                if (updatedUser.getCity() != null && !updatedUser.getCity().isBlank() && !updatedUser.getCity().equals(existingUser.getCity())) {
                    existingUser.setCity(updatedUser.getCity());
                }


                // Update journal entries
                if (updatedUser.getJournalEntries() != null) {
                    existingUser.getJournalEntries().addAll(updatedUser.getJournalEntries()); // Add new entries
                }

                // Save the updated user
                User savedUser = userRepository.save(existingUser);
                return new ResponseEntity<>(savedUser, HttpStatus.OK);
            }

            // Return NOT_FOUND if user does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<User> deleteUserById(Long id) {
        try {
            User user = userRepository.findById(id).get();
            if (user != null) {
                userRepository.deleteById(id);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteUserByName() {
        try {
            // Retrieve the currently authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String username = authentication.getName();

            // Fetch the user from the database
            User user = userRepository.findByUsername(username);
            if (user != null) {
                // Delete the user
                userRepository.deleteByUsername(username);
                // Optionally, clear sensitive information
                user.setPassword(null);
                return new ResponseEntity<>("Deletion Successful", HttpStatus.OK);
            }
            // If user not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<User> saveAdmin(User admin) {
        try {
            admin.setPassword(passwordEncoder.encode(admin.getPassword())); // Encode password before saving
            admin.setRoles(Arrays.asList("ADMIN", "USER")); // Set roles as a list
            userRepository.save(admin);
            return new ResponseEntity<>(admin, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<User> getUserDetails() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if(user != null){
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
