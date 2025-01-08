package com.example.newView.repository;

import com.example.newView.model.entity.Spot;
import com.example.newView.model.entity.SpotCategory;
import com.example.newView.model.entity.SpotType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    List<Spot> findAll();

    // Find spots by type
    List<Spot> findByType(SpotType type);

    // Find spots by category
    List<Spot> findByCategory(SpotCategory category);

    // Find spots containing name (case-insensitive)
    @Query("SELECT s FROM Spot s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Spot> findByNameContainingIgnoreCase(@Param("name") String name);

    // Find spots with stores count greater than specified number
    @Query("SELECT s FROM Spot s WHERE SIZE(s.stores) > :count")
    List<Spot> findSpotsWithMoreStoresThan(@Param("count") int count);
}
