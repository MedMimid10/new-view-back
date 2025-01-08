package com.example.newView.repository;

import com.example.newView.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUsername(String username);
    Optional<Order> findByIdAndUserUsername(Long orderId, String username);
}
