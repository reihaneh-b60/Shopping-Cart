package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.User;
import com.ecommerce.shoppingcart.Dao.UserBody;
import com.ecommerce.shoppingcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

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

    public void registerUser(UserBody userBody) throws UserExistException {

      if (userRepository.findByEmailIgnoreCase(userBody.getEmail()).isPresent()) {
          throw new UserExistException();
      }
        User user = new User();
        user.setName(userBody.getName());
        user.setEmail(userBody.getEmail());
        if (!userBody.getPassword().isEmpty())
            user.setPassword(new BCryptPasswordEncoder().encode(userBody.getPassword()));
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public User updateUser(UserBody userBody,Long id) {
        User user = userRepository.findById(id).get();
        if (userBody.getName() != null)
            user.setName(userBody.getName());
        if (userBody.getEmail()!= null)
            user.setEmail(userBody.getEmail());
        if (userBody.getPassword() != null)
            if (!userBody.getPassword().isEmpty())
                user.setPassword(new BCryptPasswordEncoder().encode(userBody.getPassword()));
        return userRepository.save(user);

    }

}
