package com.travel.fare.service;

import com.travel.fare.model.FareRequest;
import com.travel.fare.model.FareResponse;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service that implements the fare calculation business logic.
 *
 * Fare Formula: Total = BaseFare + (DistanceKm × PerKmRate)
 *
 * Rate Card:
 * ┌─────────┬───────────┬────────────┐
 * │ CabType │ Base Fare │ Per Km Rate│
 * ├─────────┼───────────┼────────────┤
 * │ MINI    │ ₹30       │ ₹8 / km    │
 * │ SEDAN   │ ₹50       │ ₹12 / km   │
 * │ SUV     │ ₹80       │ ₹18 / km   │
 * │ LUXURY  │ ₹150      │ ₹25 / km   │
 * └─────────┴───────────┴────────────┘
 */
@Service
public class FareCalculationService {

    // Base fares per cab type (in ₹)
    private static final Map<String, Double> BASE_FARES = new LinkedHashMap<>();

    // Per-km rates per cab type (in ₹)
    private static final Map<String, Double> PER_KM_RATES = new LinkedHashMap<>();

    static {
        BASE_FARES.put("MINI",   30.0);
        BASE_FARES.put("SEDAN",  50.0);
        BASE_FARES.put("SUV",    80.0);
        BASE_FARES.put("LUXURY", 150.0);

        PER_KM_RATES.put("MINI",    8.0);
        PER_KM_RATES.put("SEDAN",  12.0);
        PER_KM_RATES.put("SUV",    18.0);
        PER_KM_RATES.put("LUXURY", 25.0);
    }

    /**
     * Calculate the fare for a given request.
     *
     * @param request contains distanceKm and cabType
     * @return FareResponse with full fare breakdown
     * @throws IllegalArgumentException if cab type is unknown
     */
    public FareResponse calculateFare(FareRequest request) {
        String cabType = request.getCabType().name();

        Double baseFare = BASE_FARES.get(cabType);
        Double perKmRate = PER_KM_RATES.get(cabType);

        if (baseFare == null || perKmRate == null) {
            throw new IllegalArgumentException("Unknown cab type: " + cabType);
        }

        double totalFare = baseFare + (request.getDistanceKm() * perKmRate);

        // Round to 2 decimal places
        totalFare = Math.round(totalFare * 100.0) / 100.0;

        return new FareResponse(
                request.getDistanceKm(),
                cabType,
                baseFare,
                perKmRate,
                totalFare
        );
    }

    /**
     * Return the full rate card for all cab types.
     *
     * @return map of cabType -> { baseFare, perKmRate }
     */
    public Map<String, Map<String, Double>> getRateCard() {
        Map<String, Map<String, Double>> rateCard = new LinkedHashMap<>();
        for (String cab : BASE_FARES.keySet()) {
            Map<String, Double> rates = new LinkedHashMap<>();
            rates.put("baseFare", BASE_FARES.get(cab));
            rates.put("perKmRate", PER_KM_RATES.get(cab));
            rateCard.put(cab, rates);
        }
        return rateCard;
    }
}
