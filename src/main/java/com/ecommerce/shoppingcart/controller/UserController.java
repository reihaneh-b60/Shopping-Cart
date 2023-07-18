package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.User;
import com.ecommerce.shoppingcart.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody User user){
        try {
             userService.registerUser(user);
             return ResponseEntity.ok().build();
        } catch (UserExistException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
