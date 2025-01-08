package com.example.newView.repository;

import com.example.newView.model.entity.Souk;
import com.example.newView.model.entity.Spot;
import com.example.newView.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // Find stores by spot
    List<Store> findBySpot(Spot spot);

    // Find stores by souk
    List<Store> findBySouk(Souk souk);

    // Find stores by name containing
    List<Store> findByNameContainingIgnoreCase(String name);

    // Find stores with products count greater than
    @Query("SELECT s FROM Store s WHERE SIZE(s.products) > :count")
    List<Store> findStoresWithMoreProductsThan(@Param("count") int count);

    // Find stores by spot and name containing
    List<Store> findBySpotAndNameContainingIgnoreCase(Spot spot, String name);
}
