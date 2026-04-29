package com.travel.booking.service;

import com.travel.booking.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service layer for Passenger profile management.
 * Uses an in-memory HashMap as the data store.
 */
@Service
public class PassengerService {

    // In-memory storage
    private final Map<Long, Passenger> passengerStore = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * Create a new passenger profile.
     *
     * @param passenger the passenger to create
     * @return saved passenger with generated ID
     */
    public Passenger createPassenger(Passenger passenger) {
        long newId = idCounter.getAndIncrement();
        passenger.setId(newId);
        passengerStore.put(newId, passenger);
        return passenger;
    }

    /**
     * Get all passenger profiles.
     *
     * @return list of all passengers
     */
    public List<Passenger> getAllPassengers() {
        return new ArrayList<>(passengerStore.values());
    }

    /**
     * Get a passenger by ID.
     *
     * @param id passenger ID
     * @return Optional containing the passenger if found
     */
    public Optional<Passenger> getPassengerById(Long id) {
        return Optional.ofNullable(passengerStore.get(id));
    }

    /**
     * Update an existing passenger.
     *
     * @param id      the passenger ID to update
     * @param updated the updated passenger data
     * @return Optional containing the updated passenger, or empty if not found
     */
    public Optional<Passenger> updatePassenger(Long id, Passenger updated) {
        if (!passengerStore.containsKey(id)) {
            return Optional.empty();
        }
        updated.setId(id);
        passengerStore.put(id, updated);
        return Optional.of(updated);
    }

    /**
     * Delete a passenger profile.
     *
     * @param id passenger ID
     * @return true if deleted, false if not found
     */
    public boolean deletePassenger(Long id) {
        return passengerStore.remove(id) != null;
    }
}
