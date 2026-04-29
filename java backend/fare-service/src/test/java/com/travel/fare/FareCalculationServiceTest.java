package com.travel.fare;

import com.travel.fare.model.FareRequest;
import com.travel.fare.model.FareResponse;
import com.travel.fare.service.FareCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit test for FareCalculationService (no Spring context needed).
 * Tests the core fare formula: Total = BaseFare + (DistanceKm × PerKmRate)
 */
@DisplayName("FareCalculationService Unit Tests")
public class FareCalculationServiceTest {

    private FareCalculationService service;

    @BeforeEach
    void setUp() {
        service = new FareCalculationService();
    }

    // ── MINI ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("MINI: 10 km → ₹30 + (10 × ₹8) = ₹110")
    void testCalculateFare_MINI_10km() {
        FareRequest req = new FareRequest(10.0, FareRequest.CabType.MINI);
        FareResponse resp = service.calculateFare(req);

        assertEquals("MINI", resp.getCabType());
        assertEquals(30.0, resp.getBaseFare());
        assertEquals(8.0, resp.getPerKmRate());
        assertEquals(110.0, resp.getTotalFare(), 0.01);
        assertEquals("INR", resp.getCurrency());
    }

    @Test
    @DisplayName("MINI: 0.1 km (minimum) → ₹30 + (0.1 × ₹8) = ₹30.80")
    void testCalculateFare_MINI_minDistance() {
        FareRequest req = new FareRequest(0.1, FareRequest.CabType.MINI);
        FareResponse resp = service.calculateFare(req);
        assertEquals(30.8, resp.getTotalFare(), 0.01);
    }

    // ── SEDAN ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("SEDAN: 25.5 km → ₹50 + (25.5 × ₹12) = ₹356")
    void testCalculateFare_SEDAN_25km() {
        FareRequest req = new FareRequest(25.5, FareRequest.CabType.SEDAN);
        FareResponse resp = service.calculateFare(req);

        assertEquals("SEDAN", resp.getCabType());
        assertEquals(50.0, resp.getBaseFare());
        assertEquals(12.0, resp.getPerKmRate());
        assertEquals(356.0, resp.getTotalFare(), 0.01);
    }

    @Test
    @DisplayName("SEDAN: 0 km → ₹50 (base only)")
    void testCalculateFare_SEDAN_zeroKm() {
        FareRequest req = new FareRequest(0.0, FareRequest.CabType.SEDAN);
        FareResponse resp = service.calculateFare(req);
        assertEquals(50.0, resp.getTotalFare(), 0.01);
    }

    // ── SUV ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("SUV: 20 km → ₹80 + (20 × ₹18) = ₹440")
    void testCalculateFare_SUV_20km() {
        FareRequest req = new FareRequest(20.0, FareRequest.CabType.SUV);
        FareResponse resp = service.calculateFare(req);

        assertEquals("SUV", resp.getCabType());
        assertEquals(80.0, resp.getBaseFare());
        assertEquals(18.0, resp.getPerKmRate());
        assertEquals(440.0, resp.getTotalFare(), 0.01);
    }

    // ── LUXURY ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("LUXURY: 5 km → ₹150 + (5 × ₹25) = ₹275")
    void testCalculateFare_LUXURY_5km() {
        FareRequest req = new FareRequest(5.0, FareRequest.CabType.LUXURY);
        FareResponse resp = service.calculateFare(req);

        assertEquals("LUXURY", resp.getCabType());
        assertEquals(150.0, resp.getBaseFare());
        assertEquals(25.0, resp.getPerKmRate());
        assertEquals(275.0, resp.getTotalFare(), 0.01);
    }

    @Test
    @DisplayName("LUXURY: 100 km → ₹150 + (100 × ₹25) = ₹2650")
    void testCalculateFare_LUXURY_longDistance() {
        FareRequest req = new FareRequest(100.0, FareRequest.CabType.LUXURY);
        FareResponse resp = service.calculateFare(req);
        assertEquals(2650.0, resp.getTotalFare(), 0.01);
    }

    // ── Rate Card ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Rate card should contain all 4 cab types")
    void testGetRateCard_ContainsAllCabs() {
        Map<String, Map<String, Double>> rateCard = service.getRateCard();

        assertTrue(rateCard.containsKey("MINI"));
        assertTrue(rateCard.containsKey("SEDAN"));
        assertTrue(rateCard.containsKey("SUV"));
        assertTrue(rateCard.containsKey("LUXURY"));
        assertEquals(4, rateCard.size());
    }

    @Test
    @DisplayName("Rate card should have correct baseFare and perKmRate for each type")
    void testGetRateCard_CorrectValues() {
        Map<String, Map<String, Double>> rateCard = service.getRateCard();

        assertEquals(30.0, rateCard.get("MINI").get("baseFare"));
        assertEquals(8.0,  rateCard.get("MINI").get("perKmRate"));

        assertEquals(50.0, rateCard.get("SEDAN").get("baseFare"));
        assertEquals(12.0, rateCard.get("SEDAN").get("perKmRate"));

        assertEquals(80.0, rateCard.get("SUV").get("baseFare"));
        assertEquals(18.0, rateCard.get("SUV").get("perKmRate"));

        assertEquals(150.0, rateCard.get("LUXURY").get("baseFare"));
        assertEquals(25.0,  rateCard.get("LUXURY").get("perKmRate"));
    }

    // ── Response fields ───────────────────────────────────────────────────────

    @Test
    @DisplayName("FareResponse should always have currency INR")
    void testFareResponse_CurrencyIsINR() {
        FareRequest req = new FareRequest(15.0, FareRequest.CabType.SEDAN);
        FareResponse resp = service.calculateFare(req);
        assertEquals("INR", resp.getCurrency());
    }

    @Test
    @DisplayName("FareResponse distanceKm should match request")
    void testFareResponse_DistanceMatchesRequest() {
        double dist = 33.7;
        FareRequest req = new FareRequest(dist, FareRequest.CabType.SUV);
        FareResponse resp = service.calculateFare(req);
        assertEquals(dist, resp.getDistanceKm(), 0.001);
    }
}
