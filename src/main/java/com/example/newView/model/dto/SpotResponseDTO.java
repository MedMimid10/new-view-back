package com.example.newView.model.dto;

import com.example.newView.model.entity.Spot;
import com.example.newView.model.entity.SpotCategory;
import com.example.newView.model.entity.SpotType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String videoUrl;
    private SpotType type;
    private SpotCategory category;
    private double[] coordinates;
    private List<StoreResponseDTO> stores;

    // Summary version without stores for list views
    public static SpotResponseDTO fromEntity(Spot spot, boolean includeStores) {
        SpotResponseDTO dto = SpotResponseDTO.builder()
                .id(spot.getId())
                .name(spot.getName())
                .description(spot.getDescription())
                .imageUrl(spot.getImageUrl())
                .videoUrl(spot.getVideoUrl())
                .type(spot.getType())
                .category(spot.getCategory())
                .coordinates(spot.getCoordinates())
                .build();

        if (includeStores && spot.getStores() != null) {
            dto.setStores(spot.getStores().stream()
                    .map(store -> StoreResponseDTO.fromEntity(store, false))
                    .collect(Collectors.toList()));
        } else {
            dto.setStores(new ArrayList<>());
        }

        return dto;
    }
}
