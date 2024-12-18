package com.journal.journalApp.Controller;

import com.journal.journalApp.Entities.User;
import com.journal.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicUser {
    @Autowired
    private UserService userService;
    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        return userService.createUser(user);
    }
}
