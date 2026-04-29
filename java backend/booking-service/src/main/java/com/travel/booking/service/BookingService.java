package com.travel.booking.service;

import com.travel.booking.model.Booking;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service layer for Booking business logic.
 * Uses an in-memory HashMap as the data store.
 */
@Service
public class BookingService {

    // In-memory storage (simulates a database)
    private final Map<Long, Booking> bookingStore = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * Create a new booking.
     *
     * @param booking the booking to create
     * @return saved booking with generated ID
     */
    public Booking createBooking(Booking booking) {
        long newId = idCounter.getAndIncrement();
        booking.setId(newId);
        bookingStore.put(newId, booking);
        return booking;
    }

    /**
     * Retrieve all bookings.
     *
     * @return list of all bookings
     */
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookingStore.values());
    }

    /**
     * Retrieve a booking by its ID.
     *
     * @param id booking ID
     * @return Optional containing the booking if found
     */
    public Optional<Booking> getBookingById(Long id) {
        return Optional.ofNullable(bookingStore.get(id));
    }

    /**
     * Update an existing booking.
     *
     * @param id      the ID of the booking to update
     * @param updated the updated booking data
     * @return Optional containing the updated booking, or empty if not found
     */
    public Optional<Booking> updateBooking(Long id, Booking updated) {
        if (!bookingStore.containsKey(id)) {
            return Optional.empty();
        }
        updated.setId(id);
        bookingStore.put(id, updated);
        return Optional.of(updated);
    }

    /**
     * Delete (cancel) a booking by ID.
     *
     * @param id booking ID
     * @return true if deleted, false if not found
     */
    public boolean deleteBooking(Long id) {
        return bookingStore.remove(id) != null;
    }

    /**
     * Get bookings by passenger ID.
     *
     * @param passengerId the passenger's ID
     * @return list of bookings for the passenger
     */
    public List<Booking> getBookingsByPassenger(Long passengerId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookingStore.values()) {
            if (b.getPassengerId() != null && b.getPassengerId().equals(passengerId)) {
                result.add(b);
            }
        }
        return result;
    }
}
