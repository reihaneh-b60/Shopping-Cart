package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.Users;
import com.ecommerce.shoppingcart.dto.UserDTO;
import com.ecommerce.shoppingcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling user actions.
 */
@Service
public class UserService {

    final String UserMessage= "That user doesn't exist";
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Users saveUser(Users users) {
        return userRepository.save(users);
    }

    public Long isUserPeresent(Users users) {
        Users users1 = userRepository.findByEmailandName(users.getEmail(), users.getName());
        return users1 !=null ? users1.getId() : null;
    }

    /**
     * Attempts to register a user given the information provided.
     * @param userDTO The registration information.
     * @throws UserExistException Thrown if there is already a user with the given information.
     */
    public Users registerUser(UserDTO userDTO) throws UserExistException {

      if (userRepository.findByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
          throw new UserExistException();
      }
        Users users = new Users();
        users.setName(userDTO.getName());
        users.setEmail(userDTO.getEmail());
        if (!userDTO.getPassword().isEmpty())
            users.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        return userRepository.save(users);
    }

    /**
     * @return The list of users.
     */
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users findById(Long id) {
        userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException(UserMessage));

        return userRepository.findById(id).get();
    }

    /**
     * Update each information of specificated user.
     * @param userDTO The new information that should be replaced
     * @param id The userId that search for updating
     * @return The new information of updated user
     */
    public Users updateUser(UserDTO userDTO, Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException(UserMessage));
        if (userDTO.getName() != null)
            users.setName(userDTO.getName());
        if (userDTO.getEmail()!= null)
            users.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty())
                users.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        return userRepository.save(users);
    }

    public void deleteUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException(UserMessage));
        userRepository.deleteById(id);
    }
}
