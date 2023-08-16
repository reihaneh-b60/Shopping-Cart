package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.Users;
import com.ecommerce.shoppingcart.dto.UserBody;
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
     * @param userBody The registration information.
     * @throws UserExistException Thrown if there is already a user with the given information.
     */
    public Users registerUser(UserBody userBody) throws UserExistException {

      if (userRepository.findByEmailIgnoreCase(userBody.getEmail()).isPresent()) {
          throw new UserExistException();
      }
        Users users = new Users();
        users.setName(userBody.getName());
        users.setEmail(userBody.getEmail());
        if (!userBody.getPassword().isEmpty())
            users.setPassword(new BCryptPasswordEncoder().encode(userBody.getPassword()));
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
                .orElseThrow(()->new UsernameNotFoundException("That user doesn't exist"));

        return userRepository.findById(id).get();
    }

    /**
     * Update each information of specificated user.
     * @param userBody The new information that should be replaced
     * @param id The userId that search for updating
     * @return The new information of updated user
     */
    public Users updateUser(UserBody userBody, Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("That user doesn't exist"));
        if (userBody.getName() != null)
            users.setName(userBody.getName());
        if (userBody.getEmail()!= null)
            users.setEmail(userBody.getEmail());
        if (userBody.getPassword() != null && !userBody.getPassword().isEmpty())
                users.setPassword(new BCryptPasswordEncoder().encode(userBody.getPassword()));
        return userRepository.save(users);
    }

    public void deleteUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(()->new UsernameNotFoundException("That user doesn't exist"));
        userRepository.deleteById(id);
    }
}
