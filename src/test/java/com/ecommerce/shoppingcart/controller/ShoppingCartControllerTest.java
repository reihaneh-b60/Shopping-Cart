package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.Dao.OrderDao;
import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.model.ShoppingCart;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import com.ecommerce.shoppingcart.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getOrderTest() throws Exception {
        mvc.perform(get("/order/1")).equals(orderService.getOrderById(1L));
    }

    @Test
    public void registerOredrTest() {
        OrderDao orderDao = new OrderDao();
        orderDao.setOrderDescription("my test orders");
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        shoppingCartList.add(new ShoppingCart(1L, 3));
        shoppingCartList.add(new ShoppingCart(2L,5));
        orderDao.setCartItems(shoppingCartList);
        orderDao.setUserEmail("register$test@gmail.com");
        orderDao.setUserName("testUser");
        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();
        double exceptedAmount = product1.getPrice()*3 + product2.getPrice()*5;
        Assertions.assertEquals(exceptedAmount, orderService.getCartamount(orderDao.getCartItems()));

    }
}
