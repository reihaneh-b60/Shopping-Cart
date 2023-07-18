package com.ecommerce.shoppingcart.Dao;

import com.ecommerce.shoppingcart.model.ShoppingCart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDao {

    private String orderDescription;
    private List<ShoppingCart> cartItems;
    private String userEmail;
    private String userName;

    public OrderDao(String orderDescription, List<ShoppingCart> cartItems, String userEmail, String userName) {
        this.orderDescription = orderDescription;
        this.cartItems = cartItems;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "OrderDao{" +
                "orderDescription='" + orderDescription + '\'' +
                ", cartItems=" + cartItems +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
