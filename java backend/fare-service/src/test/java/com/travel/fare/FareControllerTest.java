package com.travel.fare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.fare.controller.FareController;
import com.travel.fare.model.FareRequest;
import com.travel.fare.model.FareResponse;
import com.travel.fare.service.FareCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for FareController using MockMvc.
 *
 * Tests:
 *  - Calculate fare for MINI
 *  - Calculate fare for SEDAN
 *  - Calculate fare for SUV
 *  - Calculate fare for LUXURY
 *  - Get rate card
 *  - Validation error (distance = 0)
 */
@WebMvcTest(FareController.class)
@DisplayName("FareController Unit Tests")
public class FareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FareCalculationService fareCalculationService;

    @Autowired
    private ObjectMapper objectMapper;

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/fare/calculate
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/fare/calculate - MINI cab for 10 km should return correct fare")
    void testCalculateFare_MINI() throws Exception {
        FareRequest request = new FareRequest(10.0, FareRequest.CabType.MINI);
        // Total = 30 + (10 × 8) = 110
        FareResponse response = new FareResponse(10.0, "MINI", 30.0, 8.0, 110.0);

        when(fareCalculationService.calculateFare(any(FareRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/fare/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cabType").value("MINI"))
                .andExpect(jsonPath("$.baseFare").value(30.0))
                .andExpect(jsonPath("$.perKmRate").value(8.0))
                .andExpect(jsonPath("$.totalFare").value(110.0))
                .andExpect(jsonPath("$.currency").value("INR"));

        verify(fareCalculationService, times(1)).calculateFare(any(FareRequest.class));
    }

    @Test
    @DisplayName("POST /api/fare/calculate - SEDAN cab for 25.5 km should return correct fare")
    void testCalculateFare_SEDAN() throws Exception {
        FareRequest request = new FareRequest(25.5, FareRequest.CabType.SEDAN);
        // Total = 50 + (25.5 × 12) = 356
        FareResponse response = new FareResponse(25.5, "SEDAN", 50.0, 12.0, 356.0);

        when(fareCalculationService.calculateFare(any(FareRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/fare/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cabType").value("SEDAN"))
                .andExpect(jsonPath("$.totalFare").value(356.0));
    }

    @Test
    @DisplayName("POST /api/fare/calculate - SUV cab for 20 km should return correct fare")
    void testCalculateFare_SUV() throws Exception {
        FareRequest request = new FareRequest(20.0, FareRequest.CabType.SUV);
        // Total = 80 + (20 × 18) = 440
        FareResponse response = new FareResponse(20.0, "SUV", 80.0, 18.0, 440.0);

        when(fareCalculationService.calculateFare(any(FareRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/fare/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cabType").value("SUV"))
                .andExpect(jsonPath("$.totalFare").value(440.0));
    }

    @Test
    @DisplayName("POST /api/fare/calculate - LUXURY cab for 5 km should return correct fare")
    void testCalculateFare_LUXURY() throws Exception {
        FareRequest request = new FareRequest(5.0, FareRequest.CabType.LUXURY);
        // Total = 150 + (5 × 25) = 275
        FareResponse response = new FareResponse(5.0, "LUXURY", 150.0, 25.0, 275.0);

        when(fareCalculationService.calculateFare(any(FareRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/fare/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cabType").value("LUXURY"))
                .andExpect(jsonPath("$.baseFare").value(150.0))
                .andExpect(jsonPath("$.totalFare").value(275.0));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/fare/rates
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/fare/rates - should return the full rate card")
    void testGetRates_Success() throws Exception {
        Map<String, Map<String, Double>> rateCard = new LinkedHashMap<>();

        Map<String, Double> mini = new LinkedHashMap<>();
        mini.put("baseFare", 30.0);
        mini.put("perKmRate", 8.0);
        rateCard.put("MINI", mini);

        Map<String, Double> sedan = new LinkedHashMap<>();
        sedan.put("baseFare", 50.0);
        sedan.put("perKmRate", 12.0);
        rateCard.put("SEDAN", sedan);

        when(fareCalculationService.getRateCard()).thenReturn(rateCard);

        mockMvc.perform(get("/api/fare/rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.MINI.baseFare").value(30.0))
                .andExpect(jsonPath("$.MINI.perKmRate").value(8.0))
                .andExpect(jsonPath("$.SEDAN.baseFare").value(50.0))
                .andExpect(jsonPath("$.SEDAN.perKmRate").value(12.0));

        verify(fareCalculationService, times(1)).getRateCard();
    }
}
