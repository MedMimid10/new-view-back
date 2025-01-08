package com.example.newView.model.dto;

import com.example.newView.model.entity.SpotCategory;
import com.example.newView.model.entity.SpotType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    private String imageUrl;
    private String videoUrl;

    @NotNull(message = "Type is required")
    private SpotType type;

    @NotNull(message = "Category is required")
    private SpotCategory category;

//    @NotNull(message = "Coordinates are required")
    private double[] coordinates;
}
