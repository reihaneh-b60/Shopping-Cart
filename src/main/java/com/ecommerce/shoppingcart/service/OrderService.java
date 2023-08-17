package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.exception.OverDemandQuantityException;
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
       return orderRepository.findById(orderId).isPresent()? orderRepository.findById(orderId).get() : null;
    }

    /**
     * Calculate the amount of each item in shopping cart and sum amount's items.
     * @param shoppingCartList
     * @return sum of the amount whole items in shopping cart
     */
    public double getCartamount(List<ShoppingCart> shoppingCartList) {
        double totalCartAmount = 0f;
        double singlecartAmount ;
        int    availableQuantity = 0;

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
     * @param myProductId
     */
    public void removeOrder(WebOrder webOrder, Long myProductId) {
        List<ShoppingCart> shoppingCartList = webOrder.getCartItems();
        List<ShoppingCart> newList = new ArrayList<>();
        for (ShoppingCart cart:shoppingCartList) {
            Long productId = cart.getProductId();
            if (productId.equals(myProductId)) {
                Product product = productRepository.findById(productId).get();
                int newQuantity = cart.getQuantity()+product.getQuantity();
                product.setQuantity(newQuantity);
            }
        }
        shoppingCartList.stream()
            .filter(cart -> myProductId.equals(cart.getProductId()))
            .forEach(newList::add);
        shoppingCartList.removeAll(newList);

        orderRepository.save(webOrder);
    }

    /**
     * change the quantity of the demand product from the order and refine the quantity of that product in the inventory and amount of the order.
     * @param order
     * @param myProductId
     * @param newCartQuantity
     */
    public void changeOrder(WebOrder order, Long myProductId, int newCartQuantity) throws OverDemandQuantityException {
        List<ShoppingCart> shoppingCartList = order.getCartItems();
        Product product = productRepository.findById(myProductId).get();
        int productQuantity;
        int differQuantity;

        for (ShoppingCart cart:shoppingCartList) {
            Long productId = cart.getProductId();
            if (productId.equals(myProductId)) {
                differQuantity = newCartQuantity - cart.getQuantity();
                if (differQuantity < product.getQuantity()) {
                    productQuantity = product.getQuantity()- (differQuantity);
                } else
                    throw new OverDemandQuantityException("Demanded Quantity is not available");

                cart.setQuantity(newCartQuantity);
                cart.setAmount(product.getPrice() * newCartQuantity);
                product.setQuantity(productQuantity);
                orderRepository.save(order);
                return;
            }
        }

        if ( newCartQuantity < product.getQuantity()) {
            ShoppingCart newShoppingCart = new ShoppingCart(myProductId, product.getName()
                    , newCartQuantity, product.getPrice() * newCartQuantity);
            shoppingCartList.add(newShoppingCart);
            product.setQuantity(product.getQuantity() - newCartQuantity);
        } else
            throw new OverDemandQuantityException("Demanded Quantity is not available");

        orderRepository.save(order);
    }

    public double getOrderAmount(List<ShoppingCart> cartItems) {
        double amount = 0;
        for (ShoppingCart shoppingCart:cartItems) {
            amount+= shoppingCart.getAmount();
        }
        return amount;
    }
}
