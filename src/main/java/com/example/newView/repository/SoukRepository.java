package com.example.newView.repository;

import com.example.newView.model.entity.Souk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoukRepository extends JpaRepository<Souk, Long> {
    // Find souks by theme
    List<Souk> findByTheme(String theme);

    // Find souks by name containing
    List<Souk> findByNameContainingIgnoreCase(String name);

    // Find souks with stores count
    @Query("SELECT s FROM Souk s WHERE SIZE(s.stores) > :count")
    List<Souk> findSouksWithMoreStoresThan(@Param("count") int count);
}
