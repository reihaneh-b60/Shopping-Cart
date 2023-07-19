package com.ecommerce.shoppingcart.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Order generated in shopping cart.
 */
@Getter
@Setter
@Entity
@ToString
@Table(name = "web_order")
public class WebOrder {

    /** Unique id for the order. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The description of the order. */
    private String orderDescription;

    /** The user of the order. */
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /** All items of the order. */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = ShoppingCart.class)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<ShoppingCart> cartItems;

    public WebOrder() {
    }

    public WebOrder(String orderDescription, User user, List<ShoppingCart> cartItems) {
        this.orderDescription = orderDescription;
        this.user = user;
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "WebOrder{" +
                "id=" + id +
                ", orderDescription='" + orderDescription + '\'' +
                ", user=" + user +
                ", cartItems=" + cartItems +
                '}';
    }
}