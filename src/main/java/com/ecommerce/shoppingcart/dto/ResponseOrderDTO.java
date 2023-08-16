package com.ecommerce.shoppingcart.dto;

import com.ecommerce.shoppingcart.model.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOrderDTO {

    private Long orderId;
    private LocalDate date;
    private String orderDescription;
    private List<ShoppingCart> cartItems;
    private double amount;



}
