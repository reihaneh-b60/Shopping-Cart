package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.model.Product;
import com.ecommerce.shoppingcart.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to handle the creation, updating & viewing of products.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    /** The Product Service. */
    private final ProductService productService;

    /**
     * Constructor for spring injection.
     * @param productService
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Gets the list of products available.
     * @return The list of products.
     */
    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

}
