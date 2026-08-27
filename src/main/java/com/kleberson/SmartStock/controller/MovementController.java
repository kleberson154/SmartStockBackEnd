package com.kleberson.SmartStock.controller;

import com.kleberson.SmartStock.dto.movement.MovementCreateRequest;
import com.kleberson.SmartStock.dto.movement.MovementResponse;
import com.kleberson.SmartStock.service.MovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class MovementController {
    private final MovementService movementService;

    @PostMapping
    public ResponseEntity<MovementResponse> create(@Valid @RequestBody MovementCreateRequest request){
        MovementResponse movement = movementService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(movement);
    }

    @GetMapping
    public ResponseEntity<List<MovementResponse>> findAll(){
        return ResponseEntity.ok(movementService.findAll());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<MovementResponse>> findByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(movementService.findByProductId(productId));
    }
}
