package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.UserDTO;
import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.Users;
import com.ecommerce.shoppingcart.dto.UserBody;
import com.ecommerce.shoppingcart.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest Controller for user data interactions.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;
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
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(users -> modelMapper.map(users, UserDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public void registerUser( @RequestBody UserBody userBody){
        try {
             userService.registerUser(userBody);
              ResponseEntity.ok().build();
        } catch (UserExistException ex) {
            ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Search a user by userId
     * @param id The Long provided by spring security context.
     * @return The specification of found user if exists
     */
    @GetMapping("/search/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(value = "id") Long id) {
        Users users = userService.findById(id);
        if (users != null) {
            UserDTO userDTO = modelMapper.map(users, UserDTO.class);
            return ResponseEntity.ok().body(userDTO);
        } else
            return null;
    }
    /**
     * Edit the user information
     * @param id The Long provided by spring security context.
     * @param userBody the user information that is changed
     * @return The specification the user after changing
     */
    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long id, @RequestBody UserBody userBody) {
         Users users = userService.updateUser(userBody,id);
         UserDTO userDTO = modelMapper.map(users, UserDTO.class);
         return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping(value = "del/{id}")
    public void deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
    }

}
