package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.Dao.OrderDao;
import com.ecommerce.shoppingcart.Dao.ResponseOrderDao;
import com.ecommerce.shoppingcart.model.User;
import com.ecommerce.shoppingcart.model.WebOrder;
import com.ecommerce.shoppingcart.service.OrderService;
import com.ecommerce.shoppingcart.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/")
public class ShoppingCartController {
    private final OrderService orderService;
    private final UserService userService;

    public ShoppingCartController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    private final Logger logger =  LoggerFactory.getLogger(ShoppingCartController.class);

    @GetMapping("/order/{orderId}")
    public ResponseEntity<WebOrder> getOrder(@PathVariable("orderId") Long orderId) {
        WebOrder order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/registerOrder")
    public ResponseEntity<ResponseOrderDao> registerOrder(@RequestBody OrderDao orderDao) {
        logger.info("Request load"+orderDao.toString());
        ResponseOrderDao responseOrderDao = new ResponseOrderDao();
        double amount = orderService.getCartamount(orderDao.getCartItems());

        User user = new User(orderDao.getUserEmail(),orderDao.getUserName());
        Long userId = userService.isUserPeresent(user);

        if (userId != null) {
            user.setId(userId);
            logger.info("user already present in users list by id"+userId);
        }else {
            user = userService.saveUser(user);
            logger.info("user saved in users list with id"+ user.getId());
        }
        WebOrder order = new WebOrder(orderDao.getOrderDescription(),user,orderDao.getCartItems());
        order = orderService.saveOrder(order);
        logger.info("order registered successfully");

        responseOrderDao.setAmount(amount);
        responseOrderDao.setDate(LocalDate.now());
        responseOrderDao.setInvoiceNumber(new Random().nextInt(500));
        responseOrderDao.setOrderId(order.getId());
        responseOrderDao.setOrderDescription(order.getOrderDescription());

        return ResponseEntity.ok(responseOrderDao);
    }

    @GetMapping("/removeOrder/{orderId}/{productId}")
    public ResponseEntity<WebOrder> removeOrder(@PathVariable("orderId") Long orderId
            ,@PathVariable("productId") Long poductId) {
        WebOrder order = orderService.getOrderById(orderId);
        orderService.removeOrder(order,poductId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/changeItem/{orderId}/{productId}/{quantity}")
    public ResponseEntity<WebOrder> changeOrder(@PathVariable("orderId") Long orderId
            ,@PathVariable("productId") Long poductId,@PathVariable("quantity") int newQuantity) {
        WebOrder order = orderService.getOrderById(orderId);
        orderService.changeOrder(order,poductId,newQuantity);
        return ResponseEntity.ok(order);
    }



}
