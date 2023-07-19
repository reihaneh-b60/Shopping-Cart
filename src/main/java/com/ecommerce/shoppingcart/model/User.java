package com.ecommerce.shoppingcart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * User for request orders
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
public class User {
    /** Unique id for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The email of the user. */
    private String email;

    /** The name of the user. */
    private String name;

    /** The encrypted password of the user. */
    private String password;

    public User() {
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}