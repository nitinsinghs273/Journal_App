package com.journal.journalApp.Controller;

import com.journal.journalApp.Entities.User;
import com.journal.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;


    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUser(){
        return userService.getAllUsers();
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<User> createAdmin(@RequestBody User admin){
        return userService.saveAdmin(admin);
    }


}
