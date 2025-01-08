package com.example.newView.controller;

import com.example.newView.model.dto.SoukRequestDTO;
import com.example.newView.model.dto.SoukResponseDTO;
import com.example.newView.service.SoukService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/souks")
public class SoukController {
    private final SoukService soukService;
    @Autowired  // optional in newer Spring versions
    public SoukController(SoukService soukService) {
        this.soukService = soukService;
    }

    @PostMapping
    public ResponseEntity<SoukResponseDTO> createSouk(@Valid @RequestBody SoukRequestDTO requestDTO) {
        System.out.println("Creating new souk");
        return ResponseEntity.ok(soukService.createSouk(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoukResponseDTO> getSouk(@PathVariable Long id) {
        System.out.println("Fetching souk with id: " + id);
        return ResponseEntity.ok(soukService.getSoukById(id));
    }

    @GetMapping
    public ResponseEntity<List<SoukResponseDTO>> getAllSouks() {
        System.out.println("Fetching all souks");
        return ResponseEntity.ok(soukService.getAllSouks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoukResponseDTO> updateSouk(
            @PathVariable Long id,
            @Valid @RequestBody SoukRequestDTO requestDTO) {
        System.out.println("Updating souk with id: " + id);
        return ResponseEntity.ok(soukService.updateSouk(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSouk(@PathVariable Long id) {
        System.out.println("Deleting souk with id: " + id);
        soukService.deleteSouk(id);
        return ResponseEntity.noContent().build();
    }
}
