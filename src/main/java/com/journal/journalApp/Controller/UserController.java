package com.journal.journalApp.Controller;

import com.journal.journalApp.Entities.User;
import com.journal.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired private UserService userService;

    @GetMapping
    public ResponseEntity<String> GreetUser(){
        return userService.GreetUser();
    }

    @GetMapping("/get-user-details")
    public ResponseEntity<User>getUserDetails(){
        return userService.getUserDetails();
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser){
        return userService.updateUser( updatedUser);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id){
        return userService.deleteUserById(id);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUserByName(){
        return userService.deleteUserByName();
    }



}
