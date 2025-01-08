package com.example.newView.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoukRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    private String theme;

    private String imageUrl;
    private String videoUrl;
}
