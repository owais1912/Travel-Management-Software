package com.travel.fare.model;

/**
 * Response DTO for a fare calculation result.
 */
public class FareResponse {

    private double distanceKm;
    private String cabType;
    private double baseFare;
    private double perKmRate;
    private double totalFare;
    private String currency;

    // ── Constructors ──────────────────────────────────────────────────────────

    public FareResponse() {}

    public FareResponse(double distanceKm, String cabType,
                        double baseFare, double perKmRate, double totalFare) {
        this.distanceKm = distanceKm;
        this.cabType = cabType;
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
        this.totalFare = totalFare;
        this.currency = "INR";
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public String getCabType() { return cabType; }
    public void setCabType(String cabType) { this.cabType = cabType; }

    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }

    public double getPerKmRate() { return perKmRate; }
    public void setPerKmRate(double perKmRate) { this.perKmRate = perKmRate; }

    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
