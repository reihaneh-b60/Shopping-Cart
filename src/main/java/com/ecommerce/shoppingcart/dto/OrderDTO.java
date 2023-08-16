package com.ecommerce.shoppingcart.dto;

import com.ecommerce.shoppingcart.model.ShoppingCart;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    private String orderDescription;
    private List<ShoppingCart> cartItems;
    private String userEmail;
    private String userName;

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderDescription='" + orderDescription + '\'' +
                ", cartItems=" + cartItems +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
