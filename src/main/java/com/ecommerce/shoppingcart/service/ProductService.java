package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling product actions.
 */
@Service
public class ProductService {

   private final ProductRepository productRepository;

    /**
     * Constructor for spring injection.
     * @param productRepository
     */
   public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

   /**
    * Gets the all products available.
    * @return The list of products.
   */
   public List<Product> getAllProducts() {
       return productRepository.findAll();
}

}
