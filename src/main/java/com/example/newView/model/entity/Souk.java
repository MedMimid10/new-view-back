package com.example.newView.model.entity;

import com.example.newView.security.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "souks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Souk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String theme;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @OneToMany(mappedBy = "souk", cascade = CascadeType.ALL)
    private List<Store> stores = new ArrayList<>();

}
