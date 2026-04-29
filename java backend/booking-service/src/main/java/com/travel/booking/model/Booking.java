package com.travel.booking.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * Booking model representing a cab booking record.
 */
public class Booking {

    private Long id;

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @NotBlank(message = "Drop location is required")
    private String dropLocation;

    @NotNull(message = "Cab type is required")
    private CabType cabType;

    @Positive(message = "Distance must be greater than zero")
    private double distanceKm;

    private BookingStatus status;

    private LocalDateTime bookingDate;

    // Cab type enumeration
    public enum CabType {
        MINI, SEDAN, SUV, LUXURY
    }

    // Booking status enumeration
    public enum BookingStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public Booking() {
        this.status = BookingStatus.CONFIRMED;
        this.bookingDate = LocalDateTime.now();
    }

    public Booking(Long id, Long passengerId, String pickupLocation,
                   String dropLocation, CabType cabType, double distanceKm) {
        this.id = id;
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.cabType = cabType;
        this.distanceKm = distanceKm;
        this.status = BookingStatus.CONFIRMED;
        this.bookingDate = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public CabType getCabType() { return cabType; }
    public void setCabType(CabType cabType) { this.cabType = cabType; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    @Override
    public String toString() {
        return "Booking{id=" + id + ", passengerId=" + passengerId +
               ", pickup='" + pickupLocation + "', drop='" + dropLocation +
               "', cab=" + cabType + ", km=" + distanceKm +
               ", status=" + status + "}";
    }
}
