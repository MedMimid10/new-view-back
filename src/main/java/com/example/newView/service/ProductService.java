package com.example.newView.service;

import com.example.newView.exception.InvalidOperationException;
import com.example.newView.exception.ResourceNotFoundException;
import com.example.newView.model.dto.ProductRequestDTO;
import com.example.newView.model.dto.ProductResponseDTO;
import com.example.newView.model.entity.Product;
import com.example.newView.model.entity.Store;
import com.example.newView.repository.ProductRepository;
import com.example.newView.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        System.out.println("Service: Creating new product with name: " + requestDTO.getName());

        Store store = storeRepository.findById(requestDTO.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + requestDTO.getStoreId()));

        Product product = Product.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .price(requestDTO.getPrice())
                .imageUrl(requestDTO.getImageUrl())
                .stockQuantity(requestDTO.getStockQuantity())
                .category(requestDTO.getCategory())
                .store(store)
                .build();

        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    public ProductResponseDTO getProductById(Long id) {
        System.out.println("Service: Fetching product with id: " + id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return ProductResponseDTO.fromEntity(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        System.out.println("Service: Fetching all products");
        return productRepository.findAll().stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByStore(Long storeId) {
        System.out.println("Service: Fetching products for store id: " + storeId);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));

        return productRepository.findByStore(store).stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        System.out.println("Service: Updating product with id: " + id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Store store = storeRepository.findById(requestDTO.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + requestDTO.getStoreId()));

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setImageUrl(requestDTO.getImageUrl());
        product.setStockQuantity(requestDTO.getStockQuantity());
        product.setCategory(requestDTO.getCategory());
        product.setStore(store);

        Product updatedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(updatedProduct);
    }

    @Transactional
    public Product updateStock(Long productId, Integer quantityChange) {
        System.out.println("Service: Updating stock for product id: " + productId + " with change: " + quantityChange);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        int newQuantity = product.getStockQuantity() + quantityChange;

        if (newQuantity < 0) {
            throw new InvalidOperationException("Insufficient stock for product: " + product.getName());
        }

        product.setStockQuantity(newQuantity);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        System.out.println("Service: Deleting product with id: " + id);
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}