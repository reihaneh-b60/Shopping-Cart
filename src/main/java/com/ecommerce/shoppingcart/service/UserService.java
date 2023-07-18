package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.User;
import com.ecommerce.shoppingcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Long isUserPeresent(User user) {
        User user1 = userRepository.findByEmailandName(user.getEmail(),user.getName());
        return user1!=null ? user1.getId() : null;
    }

    public User registerUser(User user) throws UserExistException {
      if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
          throw new UserExistException();
      }
        return userRepository.save(user);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
}
