package com.ecommerce.shoppingcart.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
/**
 * A product available for ordering.
 */
@Getter
@Setter
@Entity
public class Product {

    /** Unique id for the product. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name of the product. */
    private String name;

    /** The description of the product. */
    private String description;

    /** The price of the product. */
    private Double price;

    /** The available quantity of the product. */
    private int quantity;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", Quantity=" + quantity +
                '}';
    }
}