package com.example.newView.controller;

import com.example.newView.model.dto.ProductRequestDTO;
import com.example.newView.model.dto.ProductResponseDTO;
import com.example.newView.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired  // optional in newer Spring versions
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        System.out.println("Creating new product");
        return ResponseEntity.ok(productService.createProduct(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        System.out.println("Fetching product with id: " + id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        System.out.println("Fetching all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStore(@PathVariable Long storeId) {
        System.out.println("Fetching products for store id: " + storeId);
        return ResponseEntity.ok(productService.getProductsByStore(storeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        System.out.println("Updating product with id: " + id);
        return ResponseEntity.ok(productService.updateProduct(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        System.out.println("Deleting product with id: " + id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}