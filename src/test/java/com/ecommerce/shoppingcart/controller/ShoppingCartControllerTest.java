package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.OrderDTO;
import com.ecommerce.shoppingcart.dto.ResponseOrderDTO;
import com.ecommerce.shoppingcart.model.ShoppingCart;
import com.ecommerce.shoppingcart.model.Users;
import com.ecommerce.shoppingcart.model.WebOrder;
import com.ecommerce.shoppingcart.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShoppingCartController shoppingCartController;

    final private ModelMapper modelMapper = new ModelMapper();

    protected List<ShoppingCart> shoppingCartList = new ArrayList<>();
    @Test
    public void getOrderTest() throws Exception {
        mvc.perform(get("/order/1")).equals(orderService.getOrderById(1L));
    }

    @Test
    public void registerOrderTest() throws Exception {

        OrderDTO orderDTO = getOrder();
        WebOrder order = new WebOrder(orderDTO.getOrderDescription(),
                new Users(orderDTO.getUserEmail(),orderDTO.getUserName()), orderDTO.getCartItems());

        when(shoppingCartController.registerOrder(orderDTO))
                .thenReturn(ResponseEntity.ok().body(modelMapper.map(order, ResponseOrderDTO.class)));

        mvc.perform(post("/registerOrder").content(asJson(order)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
         }

    @Test
    public void removeOrderTest() throws Exception {

        OrderDTO orderDTO = getOrder();
        WebOrder order = new WebOrder(orderDTO.getOrderDescription(),new Users(), orderDTO.getCartItems());

        when(shoppingCartController.removeOrder(1L,1L))
                .thenReturn(ResponseEntity.ok().body(modelMapper.map(order, ResponseOrderDTO.class)));

        mvc.perform(get("/removeOrder/1/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void updateOrderTest() throws Exception {
        OrderDTO orderDTO = getOrder();
        WebOrder order = new WebOrder(orderDTO.getOrderDescription(), new Users(), orderDTO.getCartItems());

        when(shoppingCartController.changeOrder(1L,1L,5))
                .thenReturn(ResponseEntity.ok().body(modelMapper.map(order, ResponseOrderDTO.class)));

        mvc.perform(put("/changeItem/1/1/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    private static String asJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    OrderDTO getOrder(){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderDescription("my test orders");
        shoppingCartList.add(new ShoppingCart(1L, 2));
        shoppingCartList.add(new ShoppingCart(2L,3));
        orderDTO.setCartItems(shoppingCartList);
        orderDTO.setUserEmail("register$test@gmail.com");
        orderDTO.setUserName("testUser");
        return orderDTO;
    }
}
