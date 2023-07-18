package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.model.ShoppingCart;
import com.ecommerce.shoppingcart.model.WebOrder;
import com.ecommerce.shoppingcart.repository.OrderRepository;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

   private OrderRepository orderRepository;

   private ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    public WebOrder getOrderById(Long orderId) {
       Optional<WebOrder> order =orderRepository.findById(orderId);
       return order.isPresent()? order.get() : null;
    }

    public double getCartamount(List<ShoppingCart> shoppingCartList) {
       double totalCartAmount = 0f;
       double singlecartAmount = 0f;
       int availableQuantity = 0;

        for (ShoppingCart cart:shoppingCartList) {
            Long productId = cart.getProductId();
            Optional<Product> product = productRepository.findById(productId);
            if (product.isPresent()) {
                Product product1 = product.get();
                if (product1.getQuantity() < cart.getQuantity()) {
                    singlecartAmount = product1.getPrice()*product1.getQuantity();
                    cart.setQuantity(product1.getQuantity());
                }else {
                    singlecartAmount = cart.getQuantity()* product1.getPrice();
                    availableQuantity = product1.getQuantity() - cart.getQuantity();
                }
                totalCartAmount = totalCartAmount + singlecartAmount;
                product1.setQuantity(availableQuantity);
                availableQuantity=0;
                cart.setProductName(product1.getName());
                cart.setAmount(singlecartAmount);
                productRepository.save(product1);
            }
        }
        return totalCartAmount;
    }

    public WebOrder saveOrder(WebOrder order) {
       return orderRepository.save(order);
    }

//    public List<WebOrder> getOrders(Optional<User> user) {
//        return orderRepository.findByUser(user);
//    }
}
