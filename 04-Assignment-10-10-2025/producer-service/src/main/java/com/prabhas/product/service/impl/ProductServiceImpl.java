package com.prabhas.product.service.impl;

import com.prabhas.product.dto.ProductRequest;
import com.prabhas.product.dto.ProductResponse;
import com.prabhas.product.model.Product;
import com.prabhas.product.repository.ProductRepository;
import com.prabhas.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        log.info("Creating product: {}", productRequest.getName());

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .category(productRequest.getCategory())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());

        return mapToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        log.info("Fetching product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new RuntimeException("Product not found with ID: " + productId);
                });

        return mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");

        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);

        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        log.info("Updating product with ID: {}", productId);

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new RuntimeException("Product not found with ID: " + productId);
                });

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStockQuantity(productRequest.getStockQuantity());
        existingProduct.setCategory(productRequest.getCategory());
        existingProduct.setUpdatedDate(LocalDateTime.now());

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());

        return mapToProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("Deleting product with ID: {}", productId);

        if (!productRepository.existsById(productId)) {
            log.error("Product not found with ID: {}", productId);
            throw new RuntimeException("Product not found with ID: " + productId);
        }

        productRepository.deleteById(productId);
        log.info("Product deleted successfully with ID: {}", productId);
    }

    @Override
    public List<ProductResponse> searchProducts(String name) {
        log.info("Searching products by name: {}", name);

        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                .build();
    }
}