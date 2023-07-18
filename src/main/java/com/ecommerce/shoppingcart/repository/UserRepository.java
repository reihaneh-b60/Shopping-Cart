package com.ecommerce.shoppingcart.repository;


import com.ecommerce.shoppingcart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Override
    boolean existsById(Long aLong);

    Optional<User> findByEmailIgnoreCase(String email);

    @Query("select u from User u where u.email=:Email and u.name=:Name")
    User findByEmailandName(@Param(("Email")) String email,@Param("Name") String name);

}
