package com.ecommerce.shoppingcart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User for request orders
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
public class Users {
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

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private WebOrder webOrder;

    public Users() {
    }

    public Users(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}