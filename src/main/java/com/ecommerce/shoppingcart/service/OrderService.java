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

/**
 * Service for handling order actions.
 */
@Service
public class OrderService {

   private final OrderRepository orderRepository;

   private final ProductRepository productRepository;

    /**
     * Constructor for spring injection.
     * @param orderRepository
     * @param productRepository
     */
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
   }

    public WebOrder getOrderById(Long orderId) {
       Optional<WebOrder> order =orderRepository.findById(orderId);
       return order.isPresent()? order.get() : null;
    }

    /**
     * Calculate the amount of each item in shopping cart and sum amount's items.
     * @param shoppingCartList
     * @return sum of the amount whole items in shopping cart
     */
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

    /**
     * Save and register the new order.
     * @param order
     * @return saved order
     */
    public WebOrder saveOrder(WebOrder order) {
       return orderRepository.save(order);
    }

    /**
     * Remove the demand product from the order and refine the quantity of that product in the inventory and amount of the order.
     * @param webOrder
     * @param myproductId
     */
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

    /**
     * change the quantity of the demand product from the order and refine the quantity of that product in the inventory and amount of the order.
     * @param order
     * @param myproductId
     * @param newCartQuantity
     */
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
