package com.example.newView.service;

import com.example.newView.exception.ResourceNotFoundException;
import com.example.newView.model.dto.StoreRequestDTO;
import com.example.newView.model.dto.StoreResponseDTO;
import com.example.newView.model.entity.Souk;
import com.example.newView.model.entity.Spot;
import com.example.newView.model.entity.Store;
import com.example.newView.repository.SoukRepository;
import com.example.newView.repository.SpotRepository;
import com.example.newView.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final SpotRepository spotRepository;
    private final SoukRepository soukRepository;

    public StoreResponseDTO createStore(StoreRequestDTO requestDTO) {
        System.out.println("Service: Creating new store with name: " + requestDTO.getName());

        Store store = Store.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .imageUrl(requestDTO.getImageUrl())
                .profileImageUrl(requestDTO.getProfileImageUrl())
                .products(new ArrayList<>())
                .build();

        if (requestDTO.getSpotId() != null ) {
            Spot spot = spotRepository.findById(requestDTO.getSpotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Spot not found with id: " + requestDTO.getSpotId()));
            store.setSpot(spot);
        }

        if (requestDTO.getSoukId() != null) {
            Souk souk = soukRepository.findById(requestDTO.getSoukId())
                    .orElseThrow(() -> new ResourceNotFoundException("Souk not found with id: " + requestDTO.getSoukId()));
            store.setSouk(souk);
        }

        Store savedStore = storeRepository.save(store);
        return StoreResponseDTO.fromEntity(savedStore, true);
    }

    public StoreResponseDTO getStoreById(Long id) {
        System.out.println("Service: Fetching store with id: " + id);
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        return StoreResponseDTO.fromEntity(store, true);
    }

    public List<StoreResponseDTO> getAllStores() {
        System.out.println("Service: Fetching all stores");
        return storeRepository.findAll().stream()
                .map(store -> StoreResponseDTO.fromEntity(store, false))
                .collect(Collectors.toList());
    }

    public List<StoreResponseDTO> getStoresBySpot(Long spotId) {
        System.out.println("Service: Fetching stores for spot id: " + spotId);
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new ResourceNotFoundException("Spot not found with id: " + spotId));

        return storeRepository.findBySpot(spot).stream()
                .map(store -> StoreResponseDTO.fromEntity(store, false))
                .collect(Collectors.toList());
    }

    public StoreResponseDTO updateStore(Long id, StoreRequestDTO requestDTO) {
        System.out.println("Service: Updating store with id: " + id);
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));

        store.setName(requestDTO.getName());
        store.setDescription(requestDTO.getDescription());
        store.setImageUrl(requestDTO.getImageUrl());
        store.setProfileImageUrl(requestDTO.getProfileImageUrl());

        if (requestDTO.getSpotId() != null) {
            Spot spot = spotRepository.findById(requestDTO.getSpotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Spot not found with id: " + requestDTO.getSpotId()));
            store.setSpot(spot);
        }

        if (requestDTO.getSoukId() != null) {
            Souk souk = soukRepository.findById(requestDTO.getSoukId())
                    .orElseThrow(() -> new ResourceNotFoundException("Souk not found with id: " + requestDTO.getSoukId()));
            store.setSouk(souk);
        } else {
            store.setSouk(null);
        }

        Store updatedStore = storeRepository.save(store);
        return StoreResponseDTO.fromEntity(updatedStore, true);
    }

    public void deleteStore(Long id) {
        System.out.println("Service: Deleting store with id: " + id);
        if (!storeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Store not found with id: " + id);
        }
        storeRepository.deleteById(id);
    }
}