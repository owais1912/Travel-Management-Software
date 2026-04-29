package com.travel.booking.controller;

import com.travel.booking.model.Passenger;
import com.travel.booking.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Passenger profile operations.
 * Maps to /api/passengers
 */
@RestController
@RequestMapping("/api/passengers")
@CrossOrigin(origins = "*")   // Allow requests from HTML frontend
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    /**
     * POST /api/passengers
     * Create a new passenger profile.
     */
    @PostMapping
    public ResponseEntity<Passenger> createPassenger(@Valid @RequestBody Passenger passenger) {
        Passenger saved = passengerService.createPassenger(passenger);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * GET /api/passengers
     * Retrieve all passenger profiles.
     */
    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        List<Passenger> passengers = passengerService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }

    /**
     * GET /api/passengers/{id}
     * Retrieve a specific passenger by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
        Optional<Passenger> passenger = passengerService.getPassengerById(id);
        return passenger
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/passengers/{id}
     * Update an existing passenger profile.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(
            @PathVariable Long id,
            @Valid @RequestBody Passenger passenger) {
        Optional<Passenger> updated = passengerService.updatePassenger(id, passenger);
        return updated
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/passengers/{id}
     * Delete a passenger profile.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        boolean deleted = passengerService.deletePassenger(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
