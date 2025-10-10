package com.prabhas.product.controller;

import com.prabhas.product.dto.ProductRequest;
import com.prabhas.product.dto.ProductResponse;
import com.prabhas.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Received product creation request: {}", productRequest);

        try {
            ProductResponse productResponse = productService.createProduct(productRequest);
            return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        log.info("Fetching product with ID: {}", productId);

        try {
            ProductResponse productResponse = productService.getProductById(productId);
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error fetching product: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Fetching all products");

        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        log.info("Fetching products by category: {}", category);

        List<ProductResponse> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String name) {
        log.info("Searching products by name: {}", name);

        List<ProductResponse> products = productService.searchProducts(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId, 
                                                        @Valid @RequestBody ProductRequest productRequest) {
        log.info("Updating product with ID: {}", productId);

        try {
            ProductResponse productResponse = productService.updateProduct(productId, productRequest);
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error updating product: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        log.info("Deleting product with ID: {}", productId);

        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            log.error("Error deleting product: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}