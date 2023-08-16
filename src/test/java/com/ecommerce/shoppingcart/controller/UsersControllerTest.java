package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.UserBody;
import com.ecommerce.shoppingcart.dto.UserDTO;
import com.ecommerce.shoppingcart.model.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserController userController;

    final private ModelMapper modelMapper = new ModelMapper();

    @Test
    void getAllUserTest() throws Exception {

        mvc.perform(get("/user")).andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void getUserTest() throws Exception {

        given(userController.getUserById(anyLong())).willReturn(
                ResponseEntity.ok().
                        body(modelMapper.map((Users.builder().id(1L).email("test1@gmail.com").name("test1").
                                password("1234").build()), UserDTO.class)));
        mvc.perform(get("/user/search/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("test1@gmail.com"))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("test1"));
    }

    @Test
    void DeleteUserTest() throws Exception {
        Users user = getUser();
        doNothing().when(userController).deleteUser(user.getId());
        mvc.perform(delete("/user/del/"+user.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void registerUserTest() throws Exception {
        UserBody userBody = new UserBody();
        userBody.setName("test1");
        userBody.setEmail("test1@gmail.com");
        userBody.setPassword("1234");
        doNothing().when(userController).registerUser(userBody);
        mvc.perform(post("/user/register").content(asJson(userBody)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void userUpdateTest() throws Exception {
        Users users= getUser();
        when(userController.updateUser(users.getId()
                ,new UserBody(users.getName(),users.getEmail(),users.getPassword())))
                .thenReturn(ResponseEntity.ok().body(modelMapper.map(users, UserDTO.class)));
        mvc.perform(put("/user/edit/"+users.getId()).content(asJson(users)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

    }
    private static String asJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Users getUser() {
        Users users = new Users();
        users.setId(1L);
        users.setName("test1");
        users.setEmail("test1@gmail.com");
        users.setPassword("1234");
        return users;
    }
}
