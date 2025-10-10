package com.prabhas.product.service;

import java.util.List;

import com.prabhas.product.dto.ProductRequest;
import com.prabhas.product.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse getProductById(Long productId);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategory(String category);
    ProductResponse updateProduct(Long productId, ProductRequest productRequest);
    void deleteProduct(Long productId);
    List<ProductResponse> searchProducts(String name);
}