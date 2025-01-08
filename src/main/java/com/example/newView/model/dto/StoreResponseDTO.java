package com.example.newView.model.dto;

import com.example.newView.model.entity.Store;
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
public class StoreResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String profileImageUrl;
    private List<ProductResponseDTO> products;
    private Long spotId;
    private Long soukId;

    public static StoreResponseDTO fromEntity(Store store, boolean includeProducts) {
        StoreResponseDTO dto = StoreResponseDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .imageUrl(store.getImageUrl())
                .profileImageUrl(store.getProfileImageUrl())
                .spotId(store.getSpot() != null ? store.getSpot().getId() : null)
                .soukId(store.getSouk() != null ? store.getSouk().getId() : null)
                .build();

        if (includeProducts && store.getProducts() != null) {
            dto.setProducts(store.getProducts().stream()
                    .map(ProductResponseDTO::fromEntity)
                    .collect(Collectors.toList()));
        } else {
            dto.setProducts(new ArrayList<>());
        }

        return dto;
    }
}
