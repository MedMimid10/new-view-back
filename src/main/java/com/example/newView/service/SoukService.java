package com.example.newView.service;

import com.example.newView.exception.ResourceNotFoundException;
import com.example.newView.model.dto.SoukRequestDTO;
import com.example.newView.model.dto.SoukResponseDTO;
import com.example.newView.model.entity.Souk;
import com.example.newView.repository.SoukRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoukService {
    private final SoukRepository soukRepository;

    public SoukResponseDTO createSouk(SoukRequestDTO requestDTO) {
        System.out.println("Service: Creating new souk with name: " + requestDTO.getName());

        Souk souk = Souk.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .theme(requestDTO.getTheme())
                .imageUrl(requestDTO.getImageUrl())
                .videoUrl(requestDTO.getVideoUrl())
                .stores(new ArrayList<>())
                .build();

        Souk savedSouk = soukRepository.save(souk);
        return SoukResponseDTO.fromEntity(savedSouk, true);
    }

    public SoukResponseDTO getSoukById(Long id) {
        System.out.println("Service: Fetching souk with id: " + id);
        Souk souk = soukRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Souk not found with id: " + id));

        return SoukResponseDTO.fromEntity(souk, true);
    }

    public List<SoukResponseDTO> getAllSouks() {
        System.out.println("Service: Fetching all souks");
        return soukRepository.findAll().stream()
                .map(souk -> SoukResponseDTO.fromEntity(souk, false))
                .collect(Collectors.toList());
    }

    public SoukResponseDTO updateSouk(Long id, SoukRequestDTO requestDTO) {
        System.out.println("Service: Updating souk with id: " + id);
        Souk souk = soukRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Souk not found with id: " + id));

        souk.setName(requestDTO.getName());
        souk.setDescription(requestDTO.getDescription());
        souk.setTheme(requestDTO.getTheme());
        souk.setImageUrl(requestDTO.getImageUrl());
        souk.setVideoUrl(requestDTO.getVideoUrl());

        Souk updatedSouk = soukRepository.save(souk);
        return SoukResponseDTO.fromEntity(updatedSouk, true);
    }

    public void deleteSouk(Long id) {
        System.out.println("Service: Deleting souk with id: " + id);
        if (!soukRepository.existsById(id)) {
            throw new ResourceNotFoundException("Souk not found with id: " + id);
        }
        soukRepository.deleteById(id);
    }
}
