package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.model.WebOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<WebOrder, Long> {

}
