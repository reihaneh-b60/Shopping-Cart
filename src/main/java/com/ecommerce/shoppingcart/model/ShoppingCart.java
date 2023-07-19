package com.ecommerce.shoppingcart.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Every Item of an order of shopping cart.
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    /** Unique id for each item in order. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique id for product that is ordered. */
    private Long productId;

    /** The name of product that is ordered . */
    private String productName;

    /** The quantity of each item in the order. */
    private Integer quantity;

    /** The amount of each item in the order. */
    private double amount;

    public ShoppingCart(Long productId, String productName, Integer quantity, double amount) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
    }

    public ShoppingCart(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", product_id=" + productId +
                ", product_name='" + productName + '\'' +
                ", quantity=" + quantity +
                ", amount=" + amount +
                '}';
    }
}