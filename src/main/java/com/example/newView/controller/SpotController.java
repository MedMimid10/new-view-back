package com.example.newView.controller;

import com.example.newView.model.dto.SpotRequestDTO;
import com.example.newView.model.dto.SpotResponseDTO;
import com.example.newView.service.SpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class SpotController {
    private final SpotService spotService;

    @Autowired  // optional in newer Spring versions
    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }
    @PostMapping
    public ResponseEntity<SpotResponseDTO> createSpot(@Valid @RequestBody SpotRequestDTO requestDTO) {
        System.out.println("Creating new spot");
        return ResponseEntity.ok(spotService.createSpot(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotResponseDTO> getSpot(@PathVariable Long id) {
        System.out.println("Fetching spot with id: " + id);
        return ResponseEntity.ok(spotService.getSpotById(id));
    }

    @GetMapping
    public ResponseEntity<List<SpotResponseDTO>> getAllSpots() {
        System.out.println("Fetching all spots");
        return ResponseEntity.ok(spotService.getAllSpots());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpotResponseDTO> updateSpot(
            @PathVariable Long id,
            @Valid @RequestBody SpotRequestDTO requestDTO) {
        System.out.println("Updating spot with id: " + id);
        return ResponseEntity.ok(spotService.updateSpot(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Long id) {
        System.out.println("Deleting spot with id: " + id);
        spotService.deleteSpot(id);
        return ResponseEntity.noContent().build();
    }
}
