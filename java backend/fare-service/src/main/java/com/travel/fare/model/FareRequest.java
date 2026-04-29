package com.travel.fare.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO for fare calculation.
 */
public class FareRequest {

    @Positive(message = "Distance must be greater than zero")
    private double distanceKm;

    @NotNull(message = "Cab type is required")
    private CabType cabType;

    /** Supported cab types */
    public enum CabType {
        MINI, SEDAN, SUV, LUXURY
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public FareRequest() {}

    public FareRequest(double distanceKm, CabType cabType) {
        this.distanceKm = distanceKm;
        this.cabType = cabType;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public CabType getCabType() { return cabType; }
    public void setCabType(CabType cabType) { this.cabType = cabType; }
}
