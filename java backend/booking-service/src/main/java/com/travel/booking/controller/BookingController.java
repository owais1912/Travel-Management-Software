package com.travel.booking.controller;

import com.travel.booking.model.Booking;
import com.travel.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Booking operations.
 * Maps to /api/bookings
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")   // Allow requests from HTML frontend
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * POST /api/bookings
     * Create a new cab booking.
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        Booking saved = bookingService.createBooking(booking);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * GET /api/bookings
     * Retrieve all bookings.
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    /**
     * GET /api/bookings/{id}
     * Retrieve a booking by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/bookings/passenger/{passengerId}
     * Retrieve all bookings for a specific passenger.
     */
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<Booking>> getBookingsByPassenger(@PathVariable Long passengerId) {
        List<Booking> bookings = bookingService.getBookingsByPassenger(passengerId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * PUT /api/bookings/{id}
     * Update an existing booking.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody Booking booking) {
        Optional<Booking> updated = bookingService.updateBooking(id, booking);
        return updated
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/bookings/{id}
     * Cancel/delete a booking.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        boolean deleted = bookingService.deleteBooking(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
