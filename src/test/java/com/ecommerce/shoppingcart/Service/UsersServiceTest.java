package com.ecommerce.shoppingcart.Service;

import com.ecommerce.shoppingcart.dto.UserDTO;
import com.ecommerce.shoppingcart.exception.UserExistException;
import com.ecommerce.shoppingcart.model.Users;
import com.ecommerce.shoppingcart.repository.UserRepository;
import com.ecommerce.shoppingcart.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UsersServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    void getUsersTest() {
        final Users users;
        try {
            users = userService.registerUser(new UserDTO("test1","test1@gmail.com","123"));
        } catch (UserExistException e) {
            throw new RuntimeException(e);
        }
        assertEquals("test1", users.getName());
        assertEquals("test1@gmail.com", users.getEmail());
        Assertions.assertNotNull(users.getId());
    }

    @Test
    void delUserTest() {
        Users user = userRepository.save(new Users(null,"test1@gmail.com","test1","123",null));
        assertThat(user).isNotNull();
        userRepository.deleteById(user.getId());

        assertEquals(Optional.empty(),userRepository.findById(user.getId()));
    }
}
