package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.dto.OrderDTO;
import com.ecommerce.shoppingcart.dto.ResponseOrderDTO;
import com.ecommerce.shoppingcart.exception.OverDemandQuantityException;
import com.ecommerce.shoppingcart.model.Users;
import com.ecommerce.shoppingcart.model.WebOrder;
import com.ecommerce.shoppingcart.service.OrderService;
import com.ecommerce.shoppingcart.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller to handle requests to create, update and view shopping cart orders .
 */
@RestController
@RequestMapping("/")
public class ShoppingCartController {

    private final OrderService orderService;

    private final UserService userService;

    @Autowired
    private ModelMapper modelMapper;
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
    public ResponseEntity<ResponseOrderDTO> getOrder(@PathVariable("orderId") Long orderId) {

        WebOrder order = orderService.getOrderById(orderId);
        if (order != null) {
            ResponseOrderDTO responseOrderDTO = modelMapper.map(order, ResponseOrderDTO.class);
            responseOrderDTO.setAmount(orderService.getOrderAmount(order.getCartItems()));
            responseOrderDTO.setDate(LocalDate.now());

            return ResponseEntity.ok(responseOrderDTO);
        } else
            return null;
    }

    /**
     * Endpoint to register new order for specific user.
     * @param orderDTO The order specification provided by spring security context.
     * @return The details of orders that a user had made.
     */
    @PostMapping("/registerOrder")
    public ResponseEntity<ResponseOrderDTO> registerOrder(@RequestBody OrderDTO orderDTO) {
        logger.info("Request load"+ orderDTO.toString());
        ResponseOrderDTO responseOrderDTO = new ResponseOrderDTO();
        double amount = orderService.getCartamount(orderDTO.getCartItems());

        Users users = new Users(orderDTO.getUserEmail(), orderDTO.getUserName());
        Long userId = userService.isUserPeresent(users);

        if (userId != null) {
            users.setId(userId);
            logger.info("user already present in users list by id"+userId);
        }else {
            users = userService.saveUser(users);
            logger.info("user saved in users list with id"+ users.getId());
        }
        WebOrder order = new WebOrder(orderDTO.getOrderDescription(), users, orderDTO.getCartItems());
        order = orderService.saveOrder(order);
        logger.info("order registered successfully");

        responseOrderDTO.setAmount(amount);
        responseOrderDTO.setDate(LocalDate.now());
        responseOrderDTO.setOrderId(order.getId());
        responseOrderDTO.setOrderDescription(order.getOrderDescription());
        responseOrderDTO.setCartItems(orderDTO.getCartItems());

        return ResponseEntity.ok(responseOrderDTO);
    }

    /**
     * Endpoint to remove a product from user's order.
     * @param orderId The Long provided by spring security context.
     * @param productId The int provided by spring security context specify which product is removed.
     * @return The details of an order after removing the demand product.
     */
    @GetMapping("/removeOrder/{orderId}/{productId}")
    public ResponseEntity<ResponseOrderDTO> removeOrder(@PathVariable("orderId") Long orderId
            ,@PathVariable("productId") Long productId) {
        WebOrder order = orderService.getOrderById(orderId);
        orderService.removeOrder(order,productId);

        ResponseOrderDTO responseOrderDTO = modelMapper.map(order,ResponseOrderDTO.class);
        responseOrderDTO.setAmount(orderService.getOrderAmount(order.getCartItems()));
        responseOrderDTO.setDate(LocalDate.now());

        return ResponseEntity.ok(responseOrderDTO);
    }

    /**
     * Endpoint to change the quantity of a demand product from user's order.
     * @param orderId The Long provided by spring security context.
     * @param productId The int provided by spring security context specify which product is changed.
     * @return The details of an order after changing the quantity demand product.
     */
    @PutMapping("/changeItem/{orderId}/{productId}/{quantity}")
    public ResponseEntity<ResponseOrderDTO> changeOrder(@PathVariable("orderId") Long orderId
            ,@PathVariable("productId") Long productId,@PathVariable("quantity") int newQuantity) throws OverDemandQuantityException {
        WebOrder order = orderService.getOrderById(orderId);
        orderService.changeOrder(order,productId,newQuantity);
        ResponseOrderDTO responseOrderDTO = modelMapper.map(order,ResponseOrderDTO.class);
        responseOrderDTO.setAmount(orderService.getOrderAmount(order.getCartItems()));
        responseOrderDTO.setDate(LocalDate.now());

        return ResponseEntity.ok(responseOrderDTO);
    }



}
