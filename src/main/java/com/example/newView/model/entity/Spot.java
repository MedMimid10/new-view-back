package com.example.newView.model.entity;

import com.example.newView.model.dto.CoordinatesConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private SpotType type;

    @Enumerated(EnumType.STRING)
    private SpotCategory category;

    @Convert(converter = CoordinatesConverter.class)
    @Column(name = "coordinates", columnDefinition = "VARCHAR(100)")
    private double[] coordinates;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL)
    private List<Store> stores = new ArrayList<>();
}
