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
/**
 * Controller to handle requests to create, update and view shopping cart orders .
 */
@RestController
@RequestMapping("/")
public class ShoppingCartController {

    /** The Order Service. */
    private final OrderService orderService;

    /** The User Service. */
    private final UserService userService;

    /**
     * Constructor for spring injection.
     * @param orderService
     * @param userService
     */
    public ShoppingCartController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    private final Logger logger =  LoggerFactory.getLogger(ShoppingCartController.class);

    /**
     * Endpoint to get a specific order.
     * @param orderId The Long provided by spring security context.
     * @return The details of an order had made.
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<WebOrder> getOrder(@PathVariable("orderId") Long orderId) {
        WebOrder order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Endpoint to register new order for specific user.
     * @param orderDao The order specification provided by spring security context.
     * @return The details of orders that a user had made.
     */
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

    /**
     * Endpoint to remove a product from user's order.
     * @param orderId The Long provided by spring security context.
     * @param productId The int provided by spring security context specify which product is removed.
     * @return The details of an order after removing the demand product.
     */
    @GetMapping("/removeOrder/{orderId}/{productId}")
    public ResponseEntity<WebOrder> removeOrder(@PathVariable("orderId") Long orderId
            ,@PathVariable("productId") Long productId) {
        WebOrder order = orderService.getOrderById(orderId);
        orderService.removeOrder(order,productId);
        return ResponseEntity.ok(order);
    }

    /**
     * Endpoint to change the quantity of a demand product from user's order.
     * @param orderId The Long provided by spring security context.
     * @param productId The int provided by spring security context specify which product is changed.
     * @return The details of an order after changing the quantity demand product.
     */
    @GetMapping("/changeItem/{orderId}/{productId}/{quantity}")
    public ResponseEntity<WebOrder> changeOrder(@PathVariable("orderId") Long orderId
            ,@PathVariable("productId") Long productId,@PathVariable("quantity") int newQuantity) {
        WebOrder order = orderService.getOrderById(orderId);
        orderService.changeOrder(order,productId,newQuantity);
        return ResponseEntity.ok(order);
    }



}
