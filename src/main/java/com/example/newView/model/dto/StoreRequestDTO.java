package com.example.newView.model.dto;

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
public class StoreRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    private String imageUrl;
    private String profileImageUrl;
    private Long spotId;
    private Long soukId;
}
