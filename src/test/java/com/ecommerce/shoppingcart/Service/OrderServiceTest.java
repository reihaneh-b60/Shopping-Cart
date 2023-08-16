package com.ecommerce.shoppingcart.Service;

import com.ecommerce.shoppingcart.dto.OrderDTO;
import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.model.ShoppingCart;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import com.ecommerce.shoppingcart.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    protected List<ShoppingCart> shoppingCartList = new ArrayList<>();


    @Test
    public void registerOrderTest() throws NoSuchElementException {

        OrderDTO orderDTO = getOrder();
        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();
        int beforeShopQuantity1 = product1.getQuantity();
        int beforeShopQuantity2 = product2.getQuantity();


        double exceptedAmount = product1.getPrice()*2 + product2.getPrice()*3;
        assertEquals(exceptedAmount, orderService.getCartamount(orderDTO.getCartItems()));
        product1.setQuantity(product1.getQuantity() - shoppingCartList.get(0).getQuantity());
        product2.setQuantity(product2.getQuantity() - shoppingCartList.get(1).getQuantity());

        assertEquals(beforeShopQuantity1-2,product1.getQuantity());
        assertEquals(beforeShopQuantity2 - 3, product2.getQuantity());
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
