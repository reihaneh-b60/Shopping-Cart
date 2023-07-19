package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.User;
import com.ecommerce.shoppingcart.Dao.UserBody;
import com.ecommerce.shoppingcart.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for user data interactions.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for spring injection.
     * @param userService
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the list of users.
     * @return The list of users.
     */
    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Register a new user.
     * @return The specification of registered user.
     */
    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody UserBody userBody){
        try {
             userService.registerUser(userBody);
             return ResponseEntity.ok().build();
        } catch (UserExistException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Search a user by userId
     * @param id The Long provided by spring security context.
     * @return The specification of found user if exists
     */
    @GetMapping("/search/{id}")
    public @ResponseBody User searchUser(@PathVariable(value = "id") Long id) {
        return userService.findById(id);
    }
    /**
     * Edit the user information
     * @param id The Long provided by spring security context.
     * @param userBody the user information that is changed
     * @return The specification the user after changing
     */
    @PutMapping(value = "/edit/{id}")
    public User changeUser(@PathVariable(value = "id") Long id,@RequestBody UserBody userBody) {
        return userService.updateUser(userBody,id);
    }

}
