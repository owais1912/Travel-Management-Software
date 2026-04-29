package com.travel.fare.controller;

import com.travel.fare.model.FareRequest;
import com.travel.fare.model.FareResponse;
import com.travel.fare.service.FareCalculationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Fare Calculation operations.
 * Maps to /api/fare
 */
@RestController
@RequestMapping("/api/fare")
@CrossOrigin(origins = "*")   // Allow requests from HTML frontend
public class FareController {

    @Autowired
    private FareCalculationService fareCalculationService;

    /**
     * POST /api/fare/calculate
     * Calculate the total cab fare for a given distance and cab type.
     *
     * Example request body:
     * {
     *   "distanceKm": 25.5,
     *   "cabType": "SEDAN"
     * }
     */
    @PostMapping("/calculate")
    public ResponseEntity<FareResponse> calculateFare(@Valid @RequestBody FareRequest request) {
        FareResponse response = fareCalculationService.calculateFare(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/fare/rates
     * Get the full rate card (base fares and per-km rates for all cab types).
     */
    @GetMapping("/rates")
    public ResponseEntity<Map<String, Map<String, Double>>> getRates() {
        Map<String, Map<String, Double>> rateCard = fareCalculationService.getRateCard();
        return ResponseEntity.ok(rateCard);
    }
}
