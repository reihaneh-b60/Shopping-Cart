package com.ecommerce.shoppingcart.Dao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserBody {

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    @Email
    @UniqueElements
    private String email;

    @Min(4)
    private String password;
}
