package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.model.ShoppingCart;
import com.ecommerce.shoppingcart.model.WebOrder;
import com.ecommerce.shoppingcart.repository.OrderRepository;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

   private final OrderRepository orderRepository;

   private final ProductRepository productRepository;

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
       double singlecartAmount ;
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

    public void removeOrder(WebOrder webOrder, Long myproductId) {
        List<ShoppingCart> shoppingCartList = webOrder.getCartItems();
        List<ShoppingCart> newlist = new ArrayList<>();
        for (ShoppingCart cart:shoppingCartList) {
            Long productId = cart.getProductId();
            if (productId.equals(myproductId)) {
                Product product = productRepository.findById(productId).get();
                int newQuantity = cart.getQuantity()+product.getQuantity();
                product.setQuantity(newQuantity);
            }
        }
            shoppingCartList.stream()
                .filter(cart -> myproductId.equals(cart.getProductId()))
                .forEach(newlist::add);
         shoppingCartList.removeAll(newlist);

         orderRepository.save(webOrder);
    }

    public void changeOrder(WebOrder order, Long myproductId, int newCartQuantity) {
        List<ShoppingCart> shoppingCartList = order.getCartItems();
        int productQuantity = 0;
        for (ShoppingCart cart:shoppingCartList) {
            Long productId = cart.getProductId();
            if (productId.equals(myproductId)) {
                Product product = productRepository.findById(productId).get();
                if (newCartQuantity < cart.getQuantity()) {
                    productQuantity = product.getQuantity()+ (cart.getQuantity()-newCartQuantity);
                } else {
                    if (newCartQuantity < product.getQuantity()) {
                        productQuantity = product.getQuantity() -(newCartQuantity - cart.getQuantity());
                    }
                }
                cart.setQuantity(newCartQuantity);
                cart.setAmount(product.getPrice()*newCartQuantity);
                product.setQuantity(productQuantity);
                productRepository.save(product);
            }
        }
        orderRepository.save(order);
    }
}
