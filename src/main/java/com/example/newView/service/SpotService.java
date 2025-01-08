package com.example.newView.service;

import com.example.newView.exception.ResourceNotFoundException;
import com.example.newView.model.dto.SpotRequestDTO;
import com.example.newView.model.dto.SpotResponseDTO;
import com.example.newView.model.entity.Spot;
import com.example.newView.repository.SpotRepository;
import com.example.newView.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final StoreRepository storeRepository;

    public SpotResponseDTO createSpot(SpotRequestDTO requestDTO) {
        System.out.println("Service: Creating new spot with name: " + requestDTO.getName());

        Spot spot = Spot.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .imageUrl(requestDTO.getImageUrl())
                .videoUrl(requestDTO.getVideoUrl())
                .type(requestDTO.getType())
                .category(requestDTO.getCategory())
                .coordinates(requestDTO.getCoordinates())
                .stores(new ArrayList<>())
                .build();

        Spot savedSpot = spotRepository.save(spot);
        return SpotResponseDTO.fromEntity(savedSpot, true);
    }

    public SpotResponseDTO getSpotById(Long id) {
        System.out.println("Service: Fetching spot with id: " + id);
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spot not found with id: " + id));

        return SpotResponseDTO.fromEntity(spot, true);
    }

    public List<SpotResponseDTO> getAllSpots() {
        System.out.println("Service: Fetching all spots");
        return spotRepository.findAll().stream()
                .map(spot -> SpotResponseDTO.fromEntity(spot, false))
                .collect(Collectors.toList());
    }

    public SpotResponseDTO updateSpot(Long id, SpotRequestDTO requestDTO) {
        System.out.println("Service: Updating spot with id: " + id);
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spot not found with id: " + id));

        spot.setName(requestDTO.getName());
        spot.setDescription(requestDTO.getDescription());
        spot.setImageUrl(requestDTO.getImageUrl());
        spot.setVideoUrl(requestDTO.getVideoUrl());
        spot.setType(requestDTO.getType());
        spot.setCategory(requestDTO.getCategory());
        spot.setCoordinates(requestDTO.getCoordinates());

        Spot updatedSpot = spotRepository.save(spot);
        return SpotResponseDTO.fromEntity(updatedSpot, true);
    }

    public void deleteSpot(Long id) {
        System.out.println("Service: Deleting spot with id: " + id);
        if (!spotRepository.existsById(id)) {
            throw new ResourceNotFoundException("Spot not found with id: " + id);
        }
        spotRepository.deleteById(id);
    }
}