package com.example.newView.controller;

import com.example.newView.model.dto.StoreRequestDTO;
import com.example.newView.model.dto.StoreResponseDTO;
import com.example.newView.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;
    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@Valid @RequestBody StoreRequestDTO requestDTO) {
        System.out.println("Creating new store");
        return ResponseEntity.ok(storeService.createStore(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> getStore(@PathVariable Long id) {
        System.out.println("Fetching store with id: " + id);
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDTO>> getAllStores() {
        System.out.println("Fetching all stores");
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/spot/{spotId}")
    public ResponseEntity<List<StoreResponseDTO>> getStoresBySpot(@PathVariable Long spotId) {
        System.out.println("Fetching stores for spot id: " + spotId);
        return ResponseEntity.ok(storeService.getStoresBySpot(spotId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> updateStore(
            @PathVariable Long id,
            @Valid @RequestBody StoreRequestDTO requestDTO) {
        System.out.println("Updating store with id: " + id);
        return ResponseEntity.ok(storeService.updateStore(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        System.out.println("Deleting store with id: " + id);
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}