package com.example.newView.model.dto;

import com.example.newView.model.entity.Souk;
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
public class SoukResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String theme;
    private String imageUrl;
    private String videoUrl;
    private List<StoreResponseDTO> stores;

    public static SoukResponseDTO fromEntity(Souk souk, boolean includeStores) {
        SoukResponseDTO dto = SoukResponseDTO.builder()
                .id(souk.getId())
                .name(souk.getName())
                .description(souk.getDescription())
                .theme(souk.getTheme())
                .imageUrl(souk.getImageUrl())
                .videoUrl(souk.getVideoUrl())
                .build();

        if (includeStores && souk.getStores() != null) {
            dto.setStores(souk.getStores().stream()
                    .map(store -> StoreResponseDTO.fromEntity(store, false))
                    .collect(Collectors.toList()));
        } else {
            dto.setStores(new ArrayList<>());
        }

        return dto;
    }
}
