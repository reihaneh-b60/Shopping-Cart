package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.ResponseUserDTO;
import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.Users;
import com.ecommerce.shoppingcart.dto.UserDTO;
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
    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(users -> modelMapper.map(users, ResponseUserDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public void registerUser( @RequestBody UserDTO userDTO){
        try {
             userService.registerUser(userDTO);
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
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable(value = "id") Long id) {
        Users users = userService.findById(id);
        if (users != null) {
            ResponseUserDTO responseUserDTO = modelMapper.map(users, ResponseUserDTO.class);
            return ResponseEntity.ok().body(responseUserDTO);
        } else
            return null;
    }
    /**
     * Edit the user information
     * @param id The Long provided by spring security context.
     * @param userDTO the user information that is changed
     * @return The specification the user after changing
     */
    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable(value = "id") Long id, @RequestBody UserDTO userDTO) {
         Users users = userService.updateUser(userDTO,id);
         ResponseUserDTO responseUserDTO = modelMapper.map(users, ResponseUserDTO.class);
         return ResponseEntity.ok().body(responseUserDTO);
    }

    @DeleteMapping(value = "del/{id}")
    public void deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
    }

}
