package com.example.newView.repository;

import com.example.newView.model.entity.Product;
import com.example.newView.model.entity.ProductCategory;
import com.example.newView.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Find products by store
    List<Product> findByStore(Store store);

    // Find products by category
    List<Product> findByCategory(ProductCategory category);

    // Find products by price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Find products with stock less than
    List<Product> findByStockQuantityLessThan(Integer quantity);

    // Find products by store and category
    List<Product> findByStoreAndCategory(Store store, ProductCategory category);

    // Find products by name containing and price less than
    List<Product> findByNameContainingIgnoreCaseAndPriceLessThan(String name, Double maxPrice);

    // Custom query to find products with low stock (less than threshold)
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
    List<Product> findProductsWithLowStock(@Param("threshold") int threshold);
}
